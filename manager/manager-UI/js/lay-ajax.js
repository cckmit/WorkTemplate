function layAjax(ajaxOption, dataCallback, otherCallback) {

    let lay$ = $, layLayer = layer;
    if (!lay$ || !layLayer) {
        layui.use(['layer'], () => {
            lay$ = layui.jquery, layLayer = layui.layer
        });
    }

    lay$.ajax({
        url: ajaxOption.url,
        data: ajaxOption.data,
        type: ajaxOption.type,
        headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
        dataType: "json",
        async: ajaxOption.async,
        success: (result) => {
            if (result) {
                let msgOption = { icon: result.code < 0 ? 0 : result.code, time: 2000 };
                layLayer.msg(result.msg, msgOption);
                switch (result.code) {
                    // -2 表示当前访问没有权限，弹出提示即可
                    case -2:
                    // 2 表示请求正常，但是业务逻辑提示失败，弹出提示即可
                    case 2:
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
                    // 其它返回结果，可以根据业务自定义，传入result是方便根据code自定义逻辑
                    default:
                        otherCallback && otherCallback(result);
                        break;
                }
            } else {
                console.log('Ajax empty -> ', ajaxOption.url);
                layLayer.msg('请求接口正常，响应结果为空，请联系开发人员！', { icon: 2, time: 2000 });
            }
            return false;
        },
        error: (XMLHttpRequest, textStatus) => {
            console.log('Ajax error -> ', ajaxOption.url);
            layLayer.msg('请求接口异常，请联系管理员！', { icon: 2, time: 2000 });
            return false;
        },
        complete: (XMLHttpRequest, textStatus) => {
            return false;
        }

    });
}