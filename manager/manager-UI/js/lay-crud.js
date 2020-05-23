/**
 * date:2020/05/01
 * author:chen cheng
 * description: CRUD模块Table界面使用：提供条件分页查询、删除、批量删除、向Form页面传值等基础功能
 * version:1.0.1
 */
let $, layer, form, table, element;

layui.use(['table', 'form', 'layer', 'element'], () => {
    $ = layui.jquery, layer = layui.layer, form = layui.form, table = layui.table, element = layui.element;

    // 如果定义了动态下拉框，初始化数据
    const existRenderAllSelect = typeof renderAllSelect !== "undefined" && renderAllSelect !== null;
    existRenderAllSelect && renderAllSelect('windowCache');

    // 监听查询表单搜索操作
    form.on('submit(data-search-btn)', (data) => {
        tableReload(data.field);
        return false;
    });

    // 监听查询表单种的重置操作
    $("#data-search-reset").click(() => {
        $("#data-search-form")[0].reset();
        const existRenderAllSelect = typeof renderAllSelect !== "undefined" && renderAllSelect !== null;
        existRenderAllSelect && renderAllSelect('serverDb');
        setTimeout(() => {
            layer.msg('刷新完成！', { icon: 1, time: 2000 });
        }, 1000);
        return false;
    });

    // 刷新数据
    function tableReload(data) {
        console.log(data)
        if (data instanceof Object) {
            data = JSON.stringify(data);
        }
        // 表格重载
        table.reload('crudTableId', {
            page: {
                curr: 1
            },
            where: {
                queryData: data
            }
        });
    }

    // 监听表头toolBar事件
    table.on('toolbar(crudTableFilter)', (obj) => {
        switch (obj.event) {
            case 'add':
                edit({}, obj.event, 'POST', '新增');
                break;
            case 'delete':
                const data = table.checkStatus('crudTableId').data, dataLength = data.length;
                if (dataLength === 0) {
                    layer.msg('批量删除，请至少选择一行！', { icon: 0, time: 2000 });
                } else {
                    layer.confirm('真的删除选中的' + dataLength + '行么?', (index) => {
                        layer.close(index);
                        let deleteIdArray = [];
                        data.forEach((val, ind, arr) => {
                            deleteIdArray.push(val.id);
                        });
                        del(deleteIdArray);
                    });
                }
                break;
            default:
                // 系统操作：筛选列、导出、打印、提示
                const sysEvents = ["LAYTABLE_COLS", "LAYTABLE_EXPORT", "LAYTABLE_PRINT", "LAYTABLE_TIPS"];
                if (sysEvents.indexOf(obj.event) < 0) {
                    // 执行其它方法，请自行实现
                    otherToolbarEvent(obj);
                }
                break;
        }
    });

    // 监听表格复选框选择
    table.on('checkbox(crudTableFilter)', (obj) => {
        console.log(obj)
    });


    // 监听表格每行的tool操作事件
    table.on('tool(crudTableFilter)', (obj) => {
        switch (obj.event) {
            case 'edit':
                edit(obj.data, obj.event, 'PUT', '编辑');
                break;
            case 'copy':
                obj.data.id = null;
                edit(obj.data, obj.event, 'POST', '复制');
                break;
            case 'delete':
                layer.confirm('真的删除当前行么？\n删除不可恢复，请谨慎操作！', (index) => {
                    layer.close(index);
                    let deleteIdArray = [];
                    deleteIdArray.push(obj.data.id);
                    del(deleteIdArray);
                });
                break;
            default:
                // 执行其它方法，请自行实现
                otherToolEvent(obj.data);
                break;
        }
    });

    /**
     *
     * @param {Object} data
     */
    function edit(editData, type, methodType, title) {
        $('#crudFormId').removeClass('layui-hide').addClass('layui-show');
        const index = layer.open({
            type: 1,
            title: title,
            maxmin: true,
            area: ['60%', '100%'],
            content: $('#crudFormId'),
            btn: ['确定', '取消'],
            btnAlign: 'c',
            success: (layero, index) => {
                console.log(editData);
                // 可以在<script></script>内自定义私有的数据处理方法，如果不定义，则按照默认方式初始化数据
                const existRebuildFunction = typeof rebuildEditData !== "undefined" && rebuildEditData !== null;
                existRebuildFunction && rebuildEditData(editData, type);
                form.val("crudFormFilter", editData)
            },
            yes: () => {
                // 提交监听
                form.on('submit(crudSaveBtnFilter)', (submitData) => {
                    const existRebuildFunction = typeof rebuildSubmitData !== "undefined" && rebuildSubmitData !== null;
                    submitData = existRebuildFunction ? rebuildSubmitData(submitData.field) : submitData.field;
                    console.log('submitData -> ', submitData);
                    $.ajax({
                        url: currentPage.module.server + currentPage.page,
                        data: JSON.stringify(submitData),
                        headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
                        type: methodType,
                        dataType: "json",
                        xhrFields: { withCredentials: true },
                        async: false,
                        success: function (result) {
                            console.log(methodType, JSON.stringify(result));
                            parent.layer.msg(result.msg, { icon: result.code, time: 2000 });
                            if (result.code === 1) {
                                layer.close(index);
                                tableReload();
                            }
                        }
                    });
                });
                $('#crudSaveBtnId').trigger('click');
            },
            cancle: () => {
                console.log('cancle?');
            },
            end: () => {
                $("#crudFormId")[0].reset();
                $('#crudFormId').removeClass('layui-show').addClass('layui-hide');
            }
        });
        $(window).on("resize", function () {
            layer.full(index);
        });
    }

    /**
     * 删除数据
     * @param {Array} deleteIdArray 批量删除的ID数组
     */
    function del(deleteIdArray) {
        console.log(deleteIdArray);
        $.ajax({
            url: currentPage.module.server + currentPage.page + "/",
            type: 'DELETE',
            headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
            data: JSON.stringify(deleteIdArray),
            dataType: "json",
            async: false,
            success: function (result) {
                layer.msg(result.msg, { icon: result.code, time: 2000 });
                if (result.code === 1) {
                    tableReload();
                }
            }
        });
    }

})