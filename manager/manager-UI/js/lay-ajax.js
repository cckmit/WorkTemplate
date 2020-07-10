function layAjax(ajaxOption, dataCallback) {

    layui.use(['layer'], () => {
        const $ = layui.jquery, layer = layui.layer;
        $.ajax({
            url: ajaxOption.url,
            data: ajaxOption.data,
            type: ajaxOption.type,
            headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
            dataType: "json",
            async: ajaxOption.async,
            success: (result) => {
                if (result) {
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
                            setTimeout(() => {
                                window.location = '/manager/login.html';
                            }, 2000);
                            break;
                        // 1 表示成功，执行成功回调
                        case 1:
                            dataCallback && dataCallback(result.data);
                            break;
                    }
                } else {
                    console.log('Ajax error -> ', ajaxOption.url, ajaxOption.data);
                    layer.msg('请求接口正常，响应结果为空，请联系开发人员！', { icon: 2, time: 2000 });
                }
                return false;
            },
            error: (XMLHttpRequest, textStatus) => {
                console.log('Ajax error -> ', ajaxOption.url, ajaxOption.data);
                layer.msg('请求接口异常，请联系管理员！', { icon: 2, time: 2000 });
                return false;
            },
            complete: (XMLHttpRequest, textStatus) => {
                return false;
            }
        });
    });
}