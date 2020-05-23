/**
 * 表单验证
 * @param {String} value 参数
 * @param {Document} item Dom对象
 * @param {String} type 类型
 */
function formVerify(value, item, type) {
    let verifyInfo;
    if (value) {
        switch (type) {
            // 验证是否Json格式字符串
            case 'json':
                try {
                    const valueObject = JSON.parse(value);
                    if (typeof valueObject !== 'object' || !valueObject) {
                        verifyInfo = '您输入的参数不符合Json格式，请重新输入！';
                    }
                } catch (e) {
                    verifyInfo = '您输入的参数不符合Json格式，请重新输入！';
                }

                break;
            case 'url':
                try {
                    let reg = /(http|https):\/\/([\w.]+\/?)\S*/
                    let regVerify = reg.test(value);
                    let nullVerify = (value === "");
                    if (!(regVerify || nullVerify)) {
                        verifyInfo = "链接不符合，请以输入正确链接地址";
                    }
                } catch (e) {
                    verifyInfo = "链接不符合，请以输入正确链接地址";
                }

                break;
            // 验证是否中国大陆地区手机号码
            case 'cnPhone':
                break;
        }
    }
    if (verifyInfo) {
        return verifyInfo;
    } else {
        return false;
    }
}