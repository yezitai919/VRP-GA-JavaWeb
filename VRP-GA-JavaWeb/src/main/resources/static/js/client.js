new Vue({
    el: "#app",
    //钩子函数，Vue对象初始化完成后自动执行
    created() {
        //获取当前用户信息,查询所有表格信息展示出来
        this.getUser()
        this.needsPage()
        this.getDelivery()

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
            param += "&userId=" + this.user.id;
            param += "&userName=" + this.pagination.userName;
            param += "&phoneNum=" + this.pagination.phoneNum;
            param += "&driverId=" + this.pagination.driverId;
            param += "&driverName=" + this.pagination.driverName;
            param += "&driverPhone=" + this.pagination.driverPhone;
            param += "&price=" + this.pagination.price;
            param += "&arrivalTime=" + this.pagination.arrivalTime;
            param += "&state=" + this.pagination.state;
            param += "&compensation=" + this.pagination.compensation;
            param += "&remark=" + this.pagination.remark;

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
        getUser() {
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
        //新增按钮
        addNeeds() {
            /*弹窗新增窗口*/
            this.dialogVisible = true;
            /*因为和评价公用模型，所以要清空模型数据*/
            this.needs = {};
            /*把用户数据自动写上*/
            this.needs.userId = this.user.id;
            this.needs.userName = this.user.netName;
            this.needs.phoneNum = this.user.phoneNumb;
        },
        //自动计算价格
        calculatePrice() {
            if (this.needs.cliNeeds != null && this.needs.cliX != null && this.needs.cliY != null) {
                /*获取配送数据*/
                this.getDelivery();
                /*根据用户填入的位置，需求量计算价格*/
                this.needs.price = Math.sqrt(Math.abs(Math.pow(this.needs.cliX -
                        this.deliveryTable[0].deportX, 2) + Math.pow(this.needs.cliY -
                        this.deliveryTable[0].deportY, 2))) * this.deliveryTable[0].priceKm +
                    this.needs.cliNeeds * this.deliveryTable[0].priceT;
                /*计算结果更新到模型展示给用户*/
                this.needs.price = Math.round(this.needs.price * 10) / 10;
            }
        },
        /*提交新增订单*/
        needSubmit() {
            /*发送新增订单的异步请求，请求体放入订单信息模型*/
            axios.post("/needs", this.needs).then((res) => {
                if (res.data.flag) {
                    /*关闭新增窗口*/
                    this.dialogVisible = false;
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }

            }).finally(() => {
                /*刷新订单表格数据*/
                this.needsPage();
            });
        },
        //取消
        cancel() {
            /*关闭查看物流窗口，无提示*/
            this.dialogVisibleDetails = false
        },
        cancel1() {
            /*关闭新增窗口*/
            this.dialogVisible = false
            /*关闭评价窗口*/
            this.dialogVisibleEdit = false
            /*提示信息*/
            this.$message.info("已取消本次操作");
        },

        //弹出查看详情窗口
        seeDetails(row) {
            /*发送查询这行订单数据的异步请求*/
            axios.get("/needs/" + row.id).then((res) => {
                if (res.data.flag && res.data.data != null) {
                    /*弹出物流详情窗口*/
                    this.dialogVisibleDetails = true;
                    /*物流详情的信息一部分在订单表一部分在路径表，所以把需要的数据记录到一个新的模型中*/
                    this.needs = res.data.data;
                    this.logisticsDetails.id = this.needs.id;
                    this.logisticsDetails.driverName = this.needs.driverName;
                    this.logisticsDetails.driverPhone = this.needs.driverPhone;
                    this.logisticsDetails.state = this.needs.state;
                    this.logisticsDetails.totalDist = this.needs.totalDist;
                    this.logisticsDetails.currDist = this.needs.currDist;
                    let d = (( this.needs.totalDist-this.needs.currDist) / this.needs.totalDist) * 100;
                    this.logisticsDetails.position = Math.round(d * 10) / 10;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            });
            /*发送查询这个订单对应的路径的异步请求*/
            axios.get("/path/" + row.pathId).then((res) => {
                if (res.data.flag && res.data.data != null) {
                    /*获得的路径数据记录到物流详情绑定的模型中*/
                    this.pathTable = res.data.data;
                    this.logisticsDetails.plateNum = this.pathTable.plateNum;
                    this.logisticsDetails.truckName = this.pathTable.truckName;
                    this.logisticsDetails.truckType = this.pathTable.truckType;

                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            })
        },
        /*把表示路程的滑动块加上%,以百分数形式显示当前路程完成度*/
        formatTooltip(val) {
            return val+="%" ;
        },

        //弹出评价窗口
        releaseEvaluation(row) {
            /*发送异步请求获取当前订单的信息显示在弹窗上*/
            axios.get("/needs/" + row.id).then((res) => {
                if (res.data.flag && res.data.data != null) {
                    /*打开评价弹窗*/
                    this.dialogVisibleEdit = true;
                    this.needs = res.data.data;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            })
        },
        //发布或修改评价
        evaluationModify() {
            /*发送异步请求修改该订单中的评价信息*/
            axios.put("/needs", this.needs).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    /*关闭评价弹窗*/
                    this.dialogVisibleEdit = false;
                    this.$message.success("评价成功(*^ω^*)");
                } else {
                    this.$message.error("评价失败(´•̥̥̥ω•̥̥̥`)");
                }
            })
        },
        /**
         * 发送查询配送数据表所有数据的请求
         */
        getDelivery() {
            axios.get("/delivery").then((res) => {
                this.deliveryTable = res.data.data;
            });
        },
        /**
         * 发送退出登录请求，系统会删除后端的登录记录
         */
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
            // 添加数据对话框是否展示的标记
            dialogVisible: false,
            // 物流详情对话框是否展示的标记
            dialogVisibleDetails: false,
            // 评价详情对话框是否展示的标记
            dialogVisibleEdit: false,
            // 添加订单数据和评价订单数据绑定的模型，可记录一条订单数据的对象
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
                compensation: "",
                //评价信息
                evaluation: "",
                //配送速度评分(打星)
                rate1: "",
                //配送质量评分(打星)
                rate2: "",
                //服务态度评分(打星)
                rate3: "",
                //路径编号
                pathId: "",
                //订单总路程
                totalDist: "",
                //订单当前距离用户路程
                currDist: ""
            },
            /*用户信息*/
            user: {
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
                adminPhone:""
            }],
            //查看物流详情时，选中订单对应的路径信息绑定的模型
            pathTable: {
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
            },
            //物流详情对话框绑定的模型
            logisticsDetails: {
                //订单编号
                id: "",
                //配送员姓名
                driverName: "",
                //配送员电话
                driverPhone: "",
                //车牌号
                plateNum: "",
                //配送员姓名
                truckName: "",
                //配送员员电话
                truckType: "",
                //运输状态
                state: "",
                //订单总路程
                totalDist: "",
                //订单当前距离用户路程
                currDist: "",
                //配送员当前位置
                position: ""
            }
        }
    }
})
