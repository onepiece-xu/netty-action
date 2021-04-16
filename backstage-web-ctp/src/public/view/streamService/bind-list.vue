<template>
    <div class="tab-content">
        <div class="detail-row">
            <div class="detail-title">
                <a class="an-close" @click="$router.go(-1)">
                    <i class="icon-aw-mail-reply"></i>
                </a>
                <span>绑定应用列表</span>
            </div>
            <div class="detail-info">
                <div class="table-content">
                    <el-table ref="multipleTable" 
                        :data="dataList"  >
                        <el-table-column prop="id" label="应用ID" sortable>
                            <template slot-scope="scope">
                                <span :title="scope.row.id">{{scope.row.id}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="应用名称" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.name'>{{scope.row.name}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="enterpriseName" label="所属用户" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.enterpriseName'>{{scope.row.enterpriseName}}</span>
                            </template> 
                        </el-table-column>
                        <el-table-column prop="type" label="应用类型" sortable>
                            <template slot-scope="scope">
                                <span :title='scope.row.type'>{{scope.row.type}}</span>
                            </template> 
                        </el-table-column>
                    </el-table>
                    <div v-if="pageInstan.showPage">
                        <page-query v-if="pageInstan.showPage" :currentPage="pageInstan.currentPage" :total="pageInstan.Total" :pageSize="pageInstan.pageSize" @handleSizeChangeSub="handleSizeChange" @handleCurrentChangeSub="handleCurrentChange"></page-query>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</template>

<script>
import {getBindApps} from '@/public/api/streamService.js';
export default {
    data(){
        return {
            dataList:[],
            pageInstan:{//表格分页参数
                showPage:false,//是否显示分页
                Total:'',//分页总数
                currentPage:1,//当前页
                pageSize:50,//每页显示多少条
                pageSizes:[10, 20, 50, 100]//设置每页显示多少条下拉框数据
            },
        }
    },
    methods:{
        //切换页码组，每页展示多少条
        handleSizeChange(val) {
            this.pageInstan.pageSize = val;
            this.initData();
        },
        //切换页码
        handleCurrentChange(val) {
            this.pageInstan.currentPage = val;
            this.initData('','pageOne');
        },
        initData(){
            let oData = {
                'pageNum': this.pageInstan.currentPage,
                'pageSize': this.pageInstan.pageSize
            }
            let id = this.$route.query.id
            getBindApps(oData,id).then((res) =>{
                console.log(res);
                if(res&&res.data){
                    this.dataList = res.data.records;
                    if(this.dataList.length > 0){
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = true;//判断数据是否为空 为空则不显示分页
                    }else{
                        this.pageInstan.Total = res.data.total;
                        this.pageInstan.showPage = false;//判断数据是否为空 为空则不显示分页
                    }
                    this.dataList.forEach(function(item){
                        item.type = item.type == 0?'arm':'x86';
                    })
                }
            })
        },
    },
    mounted(){
        this.initData();
    }
}
</script>
