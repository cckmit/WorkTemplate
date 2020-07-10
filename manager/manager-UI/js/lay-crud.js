/**
 * date:2020/05/01
 * author:chen cheng
 * description: CRUD模块Table界面使用：提供条件分页查询、删除、批量删除、向Form页面传值等基础功能
 * version:1.0.1
 */
let $, layer, form, table, element;

layui.use(['table', 'form', 'layer', 'element'], () => {
    $ = layui.jquery, table = layui.table, form = layui.form, layer = layui.layer, element = layui.element;

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
            case 'delete': {
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
            }
                break;
            case 'remove': {
                const data = table.checkStatus('crudTableId').data, dataLength = data.length;
                if (dataLength === 0) {
                    layer.msg('批量删除，请至少选择一行！', { icon: 0, time: 2000 });
                } else {
                    layer.confirm('真的删除选中的' + dataLength + '行么?', (index) => {
                        layer.close(index);
                        let deleteIdArray = [];
                        data.forEach((val, ind, arr) => {
                            console.log("appid" + val.appId + "----" + val.minVersion)
                            deleteIdArray.push(val.appId + "-" + val.minVersion);
                        });
                        del(deleteIdArray);
                    });
                }
            }
                break;
            case 'deleteContentInfo': {
                const data = table.checkStatus('crudTableId').data, dataLength = data.length;
                if (dataLength === 0) {
                    layer.msg('批量删除，请至少选择一行！', { icon: 0, time: 2000 });
                } else {
                    layer.confirm('真的删除选中的' + dataLength + '行么?', (index) => {
                        layer.close(index);
                        let deleteIdArray = [];
                        data.forEach((val, ind, arr) => {
                            deleteIdArray.push(val.targetAppId);
                        });
                        del(deleteIdArray);
                    });
                }
            }
                break;
            default: {
                const data = table.checkStatus('crudTableId').data, dataLength = data.length;
                if (dataLength === 0) {
                    layer.msg('请至少选择一行！', { icon: 0, time: 2000 });
                } else {
                    // 系统操作：筛选列、导出、打印、提示
                    const sysEvents = ["LAYTABLE_COLS", "LAYTABLE_EXPORT", "LAYTABLE_PRINT", "LAYTABLE_TIPS"];
                    if (sysEvents.indexOf(obj.event) < 0) {
                        // 执行其它方法，请自行实现
                        otherToolbarEvent(obj);
                    }
                }
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
                otherToolEvent(obj);
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
                    $.ajax({
                        url: currentPage.module.server + currentPage.page,
                        data: JSON.stringify(submitData),
                        headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
                        type: methodType,
                        dataType: "json",
                        async: false,
                        success: function (result) {
                            const msg = result.msg ? result.msg : ((ajaxOption.type.toUpperCase() === 'GET') ? '查询成功！' : '操作成功！');
                            const msgOption = { icon: result.code < 0 ? 0 : result.code, time: 2000 };
                            parent.layer.msg(msg, msgOption);
                            switch (result.code) {
                                // -2 表示当前访问没有权限，弹出提示即可
                                case -2:
                                // 2 表示请求正常，但是业务逻辑提示失败，弹出提示即可
                                case 2:
                                default:
                                    break;
                                // -1 表示未登录，2秒后跳转登录页面
                                case -1:
                                    setTimeout(() => {
                                        window.location = '/manager/login.html';
                                    }, 2000);
                                    break;
                                // 1 表示成功，执行成功回调
                                case 1:
                                    layer.close(index);
                                    tableReload(form.val('data-search-form'));
                                    return false;
                                    break;
                            }
                        }
                    });
                    return false;
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
        $.ajax({
            url: currentPage.module.server + currentPage.page + "/",
            type: 'DELETE',
            headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
            data: JSON.stringify(deleteIdArray),
            dataType: "json",
            async: false,
            success: function (result) {
                const msg = result.msg ? result.msg : ((ajaxOption.type.toUpperCase() === 'GET') ? '查询成功！' : '操作成功！');
                const msgOption = { icon: result.code < 0 ? 0 : result.code, time: 2000 };
                layer.msg(msg, msgOption);
                switch (result.code) {
                    // -2 表示当前访问没有权限，弹出提示即可
                    case -2:
                    // 2 表示请求正常，但是业务逻辑提示失败，弹出提示即可
                    case 2:
                    default:
                        break;
                    // -1 表示未登录，2秒后跳转登录页面
                    case -1:
                        layer.close(index);
                        tableReload(form.val('data-search-form'));
                        return false;
                        break;
                    // 1 表示成功，执行成功回调
                    case 1:
                        tableReload(form.val('data-search-form'));
                        break;
                }
            }
        });
    }

})