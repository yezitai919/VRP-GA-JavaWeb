new Vue({
    el: "#app",
    //钩子函数，Vue对象初始化完成后自动执行
    created() {
        //获取当前用户信息,查询所有表格信息展示出来
        this.getUser()
        this.needsPage()
        this.getDelivery()
        this.getPath();
    }
    ,
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
            param += "&driverId=" + this.user.id;
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
            let userKey = "userInfo" + this.getUserId('value');
            //前面用户登录时把用户信息根据id存进了浏览器,现在可以根据key去浏览器中取出包含该用户信息的JSON对象
            const userInfo = window.localStorage.getItem(userKey)
            if (userInfo) {
                //保存到用户信息模型供其他功能使用
                this.user = JSON.parse(userInfo)
            }/* else {
                /!*该方法是页面初始化就调用的,如果没发现浏览器中有该用户信息,
               说明没登录直接访问该页面,给他跳到登录页面去*!/
                window.location.href = '../page/login.html'
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
        //查看评价详情
        evaluationDetails(row) {
            /*发送异步请求获取该订单信息*/
            axios.get("/needs/" + row.id).then((res) => {
                if (res.data.flag && res.data.data != null) {
                    this.dialogVisible = true;
                    this.needs = res.data.data;
                } else {
                    this.$message.error("数据同步失败，自动刷新");
                }
            });
        },
        //取消
        cancel() {
            /*关闭详情窗口*/
            this.dialogVisible = false
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
         * 发送查询路径数据表所有数据的请求
         */
        getPath() {
            axios.get("/path/driverId=" + this.user.id).then((res) => {
                this.pathTable = res.data.data;
            }).finally(() => {
                if (this.pathTable[0].currDist == null) {
                    this.pathTable[0].currDist = "未出发"
                    this.currItinerary = 0;
                } else {
                    let d = (this.pathTable[0].currDist / this.pathTable[0].totalDist) * 100;
                    this.currItinerary = Math.round(d * 10) / 10;
                }

            });
        },
        /**
         * 更新当前路程的滑动条，这里用百分比显示行程
         * @param val
         * @returns {string}
         */
        formatTooltip(val) {
            return val += "%";
        },

        /**
         * 更新配送员当前位置
         */
        currPosition() {
            this.pathTable[0].currDist = Math.round(this.pathTable[0].totalDist * (this.currItinerary / 100) * 10) / 10;
            /*发送修改路径当前位置的异步请求，后端自动更新数据*/
            axios.put("/path/position", this.pathTable[0]).then((res) => {
            }).finally(() => {
                /*更新后再刷新订单表*/
                this.needsPage();
            });
        },
        /**
         * 发送退出登录请求，系统会删除后端的登录记录
         */
        signOut() {
            this.$confirm("是否退出登录？", "退出", {type: "info"}).then(() => {
                axios.post("/user/signOut").then((res) => {

                }).finally(() => {
                    /*删除浏览器中的该用户数据*/
                    let userKey = "userInfo" + this.user.id;
                    localStorage.removeItem(userKey)
                    this.user.netName = null;
                    /*自动跳转到登录页面*/
                    window.location.href = '../page/login.html'
                });
            }).catch(() => {
                this.$message.info("已取消本次操作");
            });
        }
    },
    data() {
        return {
            // 查看详情对话框是否展示的标记
            dialogVisible: false,

            // 查看订单详情绑定的模型，可记录一条订单数据的对象
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
                id: "",
                //用户名
                userName: "",
                //密码
                password: "",
                //用户类型
                userType: "",
                //用户姓名
                netName: "---",
                //用户电话
                phoneNumb: ""
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
                adminName: "",
                //值班管理员电话
                adminPhone: ""
            }],
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
            /*当前配送员位置*/
            currItinerary: ""
        }
    }
})
