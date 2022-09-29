new Vue({

    el: "#users",

    data() {
        return {
            /*登录用户数据绑定的模型*/
            user: {
                //用户编号
                id:"",
                //用户名
                userName:"",
                //密码
                password:"",
                //用户类型
                userType:"",
            },
            /*注册用户数据绑定的模型*/
            user2:{
                //用户编号
                id:"",
                //用户名
                userName:"",
                //密码
                password:"",
                //用户类型
                userType: "客户",
                //用户姓名、昵称、网名
                netName:"",
                //用户电话
                phoneNumb:"",
                //管理员注册码
                registrationCode:""
            },
            /*管理员注册码输入框是否显示，选择注册管理员才会显示*/
            visible: false,
            /*修改用户数据绑定的模型*/
            user3:{
                //用户编号
                id:"",
                //用户名
                userName:"",
                //旧密码
                oldPassword: "",
                //新密码
                newPassword: "",
                //用户类型
                userType: "客户",
                //用户姓名、昵称、网名
                netName:"",
                //用户电话
                phoneNumb:""
            },
        }
    },
    methods: {
        /**
         * 用户登录方法
         */
        login(){
            /*发送用户登录的异步请求，请求体传入用户登录输入的数据*/
            axios.post("/user/login", this.user).then((res) => {
                if (res.data.flag) {
                    /*如果登录成功，会返回登录用户的信息，根据用户id设一个key把数据存入浏览器*/
                    let userInfo = "userInfo"+res.data.data.id;
                    localStorage.setItem(userInfo,JSON.stringify(res.data.data))
                    /*根据用户类型跳转不同页面*/
                    if (this.user.userType==="客户"){
                        /*跳转时在url上加入参数value=id，把用户的id传过去，
                        那边就可以根据id去浏览器中取到对应的用户数据*/
                        window.location.href='../page/client.html?value='+res.data.data.id;
                    }
                    if (this.user.userType==="配送员"){
                        window.location.href='../page/delivery-clerk.html?value='+res.data.data.id;
                    }
                    if (this.user.userType==="管理员"){
                        window.location.href='../page/admin.html?value='+res.data.data.id;
                    }

                } else {
                    /*登录失败提示错误信息*/
                    this.$message.error(res.data.msg);
                }
            })
        },
        /**
         * 用户注册方法
         */
        register(){
            /*发送用户注册的异步请求，请求体传入用户注册输入的数据*/
            axios.post("/user",this.user2).then((res)=>{
                if (res.data.flag){
                    this.$message.success(res.data.msg);
                }else {
                    this.$message.error(res.data.msg);
                }
            })
        },
        /**
         * 控制管理员注册码输入框是否显示
         * @param userType
         */
        showItem(userType){
            if (userType==="管理员"){
                this.visible = true;
            }else {
                this.visible = false;
                this.user2.registrationCode=null;
            }
        },
        /**
         * 用户修改方法
         */
        modifyAccount(){
            /*发送用户修改的异步请求，请求体传入用户修改输入的数据*/
            axios.put("/user",this.user3).then((res) => {
                //判断当前操作是否成功
                if (res.data.flag) {
                    this.$message.success(res.data.msg);
                } else {
                    this.$message.error(res.data.msg);
                }
            })
        }
    }
})