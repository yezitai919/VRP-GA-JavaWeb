new Vue({
    el: "#app",
    //钩子函数，Vue对象初始化完成后自动执行
    created() {
        //获取当前用户信息,查询所有表格信息展示出来
        this.getUser();
        this.needsPage();
        this.getDelivery();
        this.getGenetic();
        this.getPath();
    },
    methods: {
        //分页查询所有订单数据
        needsPage() {
            //把条件查询的参数都拼到url上，通过get请求传到后台，MybatisPlus的条件查询方法会自动排除空参数。
            let param = "?id=" + this.pagination.id;
            param += "&cliNeeds=" + this.pagination.cliNeeds;
            param += "&cliX=" + this.pagination.cliX;
            param += "&cliY=" + this.pagination.cliY;
            param += "&cliStart=" + this.pagination.cliStart;
            param += "&cliEnd=" + this.pagination.cliEnd;
            param += "&userId=" + this.pagination.userId;
            param += "&userName=" + this.pagination.userName;
            param += "&phoneNum=" + this.pagination.phoneNum;
            param += "&driverId=" + this.pagination.driverId;
            param += "&driverName=" + this.pagination.driverName;
            param += "&driverPhone=" + this.pagination.driverPhone;
            param += "&price=" + this.pagination.price;
            param += "&arrivalTime=" + this.pagination.arrivalTime;
            param += "&state=" + this.pagination.state;
            param += "&compensation=" + this.pagination.compensation;
            //发送分页查询的异步请求
            axios.get("/needs/" + this.pagination.currentPage + "/" + this.pagination.pageSize + param).then((res) => {
                //更新模型数据
                this.pagination.pageSize = res.data.data.size;
                this.pagination.currentPage = res.data.data.current;
                this.pagination.total = res.data.data.total;
                this.tableData = res.data.data.records;
            }).finally(() => {
                /*刷新列表数据,把一些列的空值该成默认值,比如到达时间查询到null就改成待定*/
                for (let i = 0; i < this.tableData.length; i++) {

                    if (this.tableData[i].arrivalTime == null) {
                        this.tableData[i].arrivalTime = "待定";
                        this.tableData[i].compensation = "待定";
                    }

                    if (this.tableData[i].driverId == null) {
                        this.tableData[i].driverId = "待定";
                        this.tableData[i].driverName = "待定";
                        this.tableData[i].driverPhone = "待定";
                    }

                }
            });
        },
        //分页工具条
        handleCurrentChange(currentPage) {
            //修改页码值为当前选中的页码值
            this.pagination.currentPage = currentPage;
            this.needsPage();
        },
        //获取用户信息
        getUser(){
            //获取url中的id拼出key
            let userKey = "userInfo"+this.getUserId('value');
            //前面用户登录时把用户信息根据id存进了浏览器,现在可以根据key去浏览器中取出包含该用户信息的JSON对象
            const userInfo = window.localStorage.getItem(userKey)
            if (userInfo) {
                //保存到用户信息模型供其他功能使用
                this.user = JSON.parse(userInfo)
            }/*else {
                /!*该方法是页面初始化就调用的,如果没发现浏览器中有该用户信息,
                说明没登录直接访问该页面,给他跳到登录页面去*!/
                window.location.href='../page/login.html'
            }*/
        },
        //获取url中的参数value=id 就是分割字符串的操作
        getUserId(names, urls) {
            urls = urls || window.location.href;
            urls && urls.indexOf("?") > -1 ? urls = urls
                .substring(urls.indexOf("?") + 1) : "";
            const reg = new RegExp("(^|&)" + names + "=([^&]*)(&|$)", "i");
            const r = urls ? urls.match(reg) : window.location.search.substr(1)
                .match(reg);
            if (r != null && r[2] !== "")
                return unescape(r[2]);
            return null;
        },


        //取消本次操作
        cancel() {
            //通用的取消方法,把弹窗都关掉
            this.dialogVisible = false
            this.dialogVisibleEdit = false
            this.dialogVisibleEditDeli = false
            this.dialogVisibleEditGene = false
            this.$message.info("已取消本次操作");
        },
        //发送删除一个订单数据的请求,参数传入表格中该行数据的对象,可获取该行数据的id
        needDelete(row) {
            //提示信息
            this.$confirm("删除后无法恢复，是否继续？", "注意", {type: "info"}).then(() => {

                //发送删除一条数据的异步请求
                axios.delete("/needs/" + row.id).then((res) => {
                    //弹出提示信息
                    if (res.data.flag) {
                        this.$message.success(res.data.msg);
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    //再次获取订单表信息,也就是刷新表格数据
                    this.needsPage();
                });
            }).catch(() => {
                this.$message.info("已取消本次操作");
            });
        },

        //弹出修改订单数据的窗口，并发送查询这行数据的请求
        needUpdate(row) {
            //发送异步请求查询该行数据,
            axios.get("/needs/" + row.id).then((res) => {
                //查询成功
                if (res.data.flag && res.data.data != null) {
                    //打开对应弹窗
                    this.dialogVisibleEdit = true;
                    //把查询数据存储到修改表单绑定的模型上,数据展示到表单上,方便进行小幅度修改
                    this.needs = res.data.data;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.needsPage();
            });
        },
        //获取到修改后的订单数据，发送修改一行订单数据请求
        needModify() {
            //发送修改数据的异步请求
            axios.put("/needs", this.needs).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    //关闭修改数据弹窗
                    this.dialogVisibleEdit = false;
                    //提示信息
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.needsPage();
            });
        },
        //发送查询配送数据表所有数据的请求
        getDelivery() {
            axios.get("/delivery").then((res) => {
                this.deliveryTable = res.data.data;
            });
        },
        //发送查询遗传参数表所有数据的请求
        getGenetic() {
            axios.get("/genetic").then((res) => {
                this.geneticTable = res.data.data;
            });
        },
        //弹出修改配送数据的窗口，并发送查询这行数据的请求
        deliveryUpdate() {
            axios.get("/delivery").then((res) => {
                if (res.data.flag && res.data.data != null) {
                    this.dialogVisibleEditDeli = true;
                    this.delivery = res.data.data[0];
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getDelivery();
            });
        },
        //获取到修改后的配送数据，发送修改配送数据请求
        deliveryModify() {
            axios.put("/delivery", this.delivery).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    //1.关闭弹层
                    this.dialogVisibleEditDeli = false;
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getDelivery();
            });

        },
        //弹出修改遗传算法参数的窗口，并发送查询这行数据的请求
        geneticUpdate() {
            axios.get("/genetic").then((res) => {
                if (res.data.flag && res.data.data != null) {
                    this.dialogVisibleEditGene = true;
                    this.genetic = res.data.data[0];
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getGenetic();
            });
        },
        //获取到修改后的遗传算法参数，发送修改遗传算法参数请求
        geneticModify() {
            axios.put("/genetic", this.genetic).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    //1.关闭弹层
                    this.dialogVisibleEditGene = false;
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getGenetic();
            });
        },
        //发出调用遗传算法计算当前路径，并更新相应数据库中的数据
        calculate() {
            axios.get("/calculate").then((res) => {
                if (res.data.flag){
                    this.geneResult = res.data.data;
                }else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                this.updateResult();

            });
        },
        //获取遗传算法计算结果，打印到网页上，并刷新订单表格和路径表格的数据
        updateResult() {
            if (this.geneResult!=null){
                this.textarea = "计算时间：" + this.geneResult.calculatingTime
                    + "毫秒    " + "最小成本：" + parseFloat(this.geneResult.minCost).toFixed(2) + "元    "
                    + "最优路径：" + this.geneResult.optimalPath;
                //再次获取订单表信息,也就是刷新表格数据
                this.needsPage();
                this.getPath();
            }
        },
        //发送查询所有路径数据的请求
        getPath() {
            axios.get("/path").then((res) => {
                this.pathTable = res.data.data;

            }).finally(() => {
                for (let i = 0; i < this.pathTable.length; i++) {
                    if (this.pathTable[i].currDist==null){
                        this.pathTable[i].currDist="未出发"
                    }
                }
            });
        },
        //弹出修改路径数据的窗口，并发送查询这行数据的请求
        pathUpdate(row) {
            axios.get("/path/"+row.id).then((res) => {
                if (res.data.flag && res.data.data != null) {
                    this.dialogVisible = true;
                    this.pathFrom = res.data.data;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getPath();
            });
        },
        //获取到修改后的路径数据，发送修改路径数据请求
        pathModify() {
            axios.put("/path", this.pathFrom).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    //1.关闭弹层
                    this.dialogVisible = false;
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            }).finally(() => {
                //再次获取订单表信息,也就是刷新表格数据
                this.getPath();
                this.needsPage();
            });
        },
        //发送退出登录请求，系统会删除后端的登录记录
        signOut(){
            this.$confirm("是否退出登录？", "退出", {type: "info"}).then(() => {
                axios.post("/user/signOut").then((res) => {
                }).finally(() => {
                    /*删除浏览器中的该用户数据*/
                    let userKey = "userInfo"+this.user.id;
                    localStorage.removeItem(userKey)
                    this.user.netName = null;
                    /*自动跳转到登录页面*/
                    window.location.href='../page/login.html'
                });
            }).catch(() => {
                this.$message.info("已取消本次操作");
            });
        }
    },
    data() {
        return {
            //修改订单数据对话框是否展示的标记
            dialogVisible: false,
            //修改配送数据对话框是否展示的标记
            dialogVisibleEdit: false,
            //修改遗传算法参数对话框是否展示的标记
            dialogVisibleEditDeli: false,
            //修改路径数据对话框是否展示的标记
            dialogVisibleEditGene: false,
            // 修改订单数据绑定的模型，可记录一条订单数据的对象
            needs: {
                //订单编号
                id: "",
                //需求量
                cliNeeds: "",
                //客户横坐标
                cliX: "",
                //客户纵坐标
                cliY: "",
                //最早收货时间
                cliStart: "",
                //最晚收货时间
                cliEnd: "",
                //客户编号
                userId: "",
                //客户姓名
                userName: "",
                //客户电话
                phoneNum: "",
                //备注信息
                remark: "",
                //配送员编号
                driverId: "",
                //配送员姓名
                driverName: "",
                //配送员电话
                driverPhone: "",
                //订单价格
                price: "",
                //到达时间
                arrivalTime: "",
                //运输状态
                state: "",
                //超时赔付金
                compensation: ""
            },
            // 当前用户的账户信息，从浏览器中获取
            user:{
                //用户编号
                id:"",
                //用户名
                userName:"",
                //密码
                password:"",
                //用户类型
                userType:"",
                //用户姓名
                netName:"---",
                //用户电话
                phoneNumb:""
            },
            // 订单数据表格绑定的模型，可记录n条订单数据的数组
            tableData: [{
                //订单编号
                id: "",
                //需求量
                cliNeeds: "",
                //客户横坐标
                cliX: "",
                //客户纵坐标
                cliY: "",
                //最早收货时间
                cliStart: "",
                //最晚收货时间
                cliEnd: "",
                //客户编号
                userId: "",
                //客户姓名
                userName: "",
                //客户电话
                phoneNum: "",
                //备注信息
                remark: "",
                //配送员编号
                driverId: "",
                //配送员姓名
                driverName: "",
                //配送员电话
                driverPhone: "",
                //订单价格
                price: "",
                //到达时间
                arrivalTime: "",
                //运输状态
                state: "",
                //超时赔付金
                compensation: ""
            }],
            //分页查询和条件搜索绑定的模型
            pagination: {
                //当前页码
                currentPage: 1,
                //每页显示的记录数
                pageSize: 5,
                //总记录数
                total: 0,
                //订单编号
                id: "",
                //需求量
                cliNeeds: "",
                //客户横坐标
                cliX: "",
                //客户纵坐标
                cliY: "",
                //最早收货时间
                cliStart: "",
                //最晚收货时间
                cliEnd: "",
                //客户编号
                userId: "",
                //客户姓名
                userName: "",
                //客户电话
                phoneNum: "",
                //备注信息
                remark: "",
                //配送员编号
                driverId: "",
                //配送员姓名
                driverName: "",
                //配送员电话
                driverPhone: "",
                //订单价格
                price: "",
                //到达时间
                arrivalTime: "",
                //运输状态
                state: "",
                //超时赔付金
                compensation: ""
            },
            // 配送数据表格绑定的模型，可记录n条配送数据的数组,暂时只有一条数据
            deliveryTable: [{
                //只有一条数据,编号没啥意义,只是为了方便修改数据
                id: "",
                //货车数量
                truck: "",
                //货车装载量
                loading: "",
                //货车速度
                speed: "",
                //每公里行驶成本
                cost: "",
                //每超一小时惩罚金额
                punish: "",
                //仓库横坐标
                deportX: "",
                //仓库纵坐标
                deportY: "",
                //每吨配送收费
                priceT: "",
                //每公里配送收费
                priceKm: "",
                //值班管理员姓名
                adminName:"",
                //值班管理员电话
                adminPhone:"",
                //管理员账号注册码
                registrationCode:""
            }],
            // 遗传算法参数表格绑定的模型，可记录n条遗传算法参数的数组,暂时只有一条数据
            geneticTable: [{
                //只有一条数据,编号没啥意义,只是为了方便修改数据
                id: "1",
                //种群数量
                popuNum: "",
                //遗传代数
                geneNum: "",
                //交叉率
                crosRate: "",
                //变异率
                mutaRate: ""
            }],
            // 修改配送数据绑定的模型，可记录一条配送数据的对象
            delivery: {
                //只有一条数据,编号没啥意义,只是为了方便修改数据
                id: "",
                //货车数量
                truck: "",
                //货车装载量
                loading: "",
                //货车速度
                speed: "",
                //每公里行驶成本
                cost: "",
                //每超一小时惩罚金额
                punish: "",
                //仓库横坐标
                deportX: "",
                //仓库纵坐标
                deportY: "",
                //每吨配送收费
                priceT: "",
                //每公里配送收费
                priceKm: "",
                //值班管理员姓名
                adminName:"",
                //值班管理员电话
                adminPhone:"",
                //管理员账号注册码
                registrationCode:""
            },
            // 修改遗传算法参数绑定的模型，可记录一条遗传算法参数的对象
            genetic: {
                //只有一条数据,编号没啥意义,只是为了方便修改数据
                id: "1",
                //种群数量
                popuNum: "",
                //遗传代数
                geneNum: "",
                //交叉率
                crosRate: "",
                //变异率
                mutaRate: ""
            },
            //遗传算法计算结果绑定的模型,发送计算请求后返回的计算结果记录在这里
            geneResult: {
                //最小成本
                minCost: "",
                //计算时间
                calculatingTime: "",
                //最优路径
                optimalPath: ""
            },
            //展示计算结果的文本框绑定的模型
            textarea: "",
            // 路径数据表格绑定的模型，可记录n条路径数据的数组
            pathTable:[{
                //路径编号
                id:"",
                //路径序列
                route:"",
                //车牌号
                plateNum:"",
                //货车名字
                truckName:"",
                //货车型号
                truckType:"",
                //配送员编号
                driverId:"",
                //配送员姓名
                driverName:"",
                //配送员员电话
                driverPhone:"",
                //总路程
                totalDist:"",
                //当前路程
                currDist:""
            }],
            // 修改路径数据绑定的模型，可记录一条路径数据的对象
            pathFrom:{
                //路径编号
                id:"",
                //路径序列
                route:"",
                //车牌号
                plateNum:"",
                //货车名字
                truckName:"",
                //货车型号
                truckType:"",
                //配送员编号
                driverId:"",
                //配送员姓名
                driverName:"",
                //配送员员电话
                driverPhone:"",
                //总路程
                totalDist:"",
                //当前路程
                currDist:""
            }
        }
    }
})
