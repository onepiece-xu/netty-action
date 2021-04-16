<template>
    <div class="tab-content">
        <el-tree
            class="filter-tree tree-checkbox"
            :data="data"
            :props="defaultProps"
            node-key="id"
            :render-content="renderContent"
            ref="tree2">
        </el-tree>
    </div>
</template>
<script>
export default {
    data() {
        return {
            defaultProps: {
                children: 'users',
                label: 'groupName'
            },
            data:[]
        }
    },
    props: ["dataList"],
    methods: {
        renderContent(h, { node }) {
            console.log(node);
            // 判断是否是最后一个节点
            if(node.childNodes.length>0 || node.data.users){
                return (
                    <span>
                        <i class="icon-aw-folder2"></i>
                        <span></span>
                        <span>{node.label}</span>
                    </span>
                )
            }else{
                return (
                    <p class="ipitem" value={node.data.id}>
                        <div class="checkbox">
                            <label>
                                <span class="iname">{node.data.loginName}</span>
                                <input type="radio" name="value" value={node.data.id} checked={node.data.checked} on-change={(e) =>this.changeBox(node,e)} />
                                <i class="icon-aw-circle"></i> 
                            </label>
                        </div>
                    </p>
                )
            }
                
        },
        changeBox(node,e){
            node.data.checked = e.target.checked
            this.$emit("nodeCheck",{'node':node});
        },
    },
    watch:{
        dataList:{
            
            handler(newName) {
                console.log(newName);
                this.data = newName?newName:this.data;
            },
            immediate: true,
            deep: true
        }
    },
    mounted () {
        console.log(this.dataList)
    }
}

</script>