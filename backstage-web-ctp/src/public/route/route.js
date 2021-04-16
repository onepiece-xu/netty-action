import VueRouter from "vue-router";
import mainMenu from "../common-tmpl/index.vue";


const routes = [
    {
        path:"/",
        component:mainMenu,
        redirect: '/login',
        children: [
            //设备管理
            {
                path: "/query",
                component:() => import('../view/query/index.vue'),
                name:'query',
                meta:{
                    keyword:"query",
                    heighlightword:"colony",
                    parent:"query",
                    showBcn:true
                },
                children:[
                    
                ]
            },
            // 计量
            {
                path: "/metering",
                component:() => import('../view/metering/index.vue'),
                name:'metering',
                meta:{
                    keyword:"metering",
                    heighlightword:"metering",
                    parent:"metering",
                    showBcn:false
                },
                children:[
                    
                ]
            },          
            
        ]
    },
    {
        path:"/login",
        component:() => import('../view/login/index.vue'),
        name:"login",
        meta:{
            keyword:"login",
            heighlightword:"login",
            parent:"login",
        },
    },
    {
        path: "/run",
        name: "run",
        component:() => import('../view/run/index.vue'),
    }
];

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
    routes // (缩写) 相当于 routes: routes
});
export {router};
