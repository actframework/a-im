import $ from 'jquery'

function setUpAjaxInterceptor() {

    var callbackWithAjaxRedirect = function(realCallback) {
        return function() {
            var jqXHR = arguments[2]
            if (jqXHR.status === 278) {
                window.location = jqXHR.getResponseHeader('Location')
            }
            if (realCallback) realCallback.apply(this, arguments)
        }
    }

    $.each(['get', 'post', 'put', 'delete', 'patch' ], function (i, method) {
        $[ method ] = function (url, data, callback, type) {
            // shift arguments if data argument was omitted
            if ($.isFunction(data)) {
                type = type || callback
                callback = data
                data = undefined
            }

            var submitMethod = method

            if (method === 'put' || method === 'patch' || method === 'delete') {
                var hasParam = url.contains('?')
                if (!hasParam) url = url + '?_method=' + method
                if (data) $.each(data, function(key, val){
                    url = url + '&' + encodeURIComponent(key) + '=' + encodeURIComponent(val)
                })
                data = {}
                submitMethod = 'post'
            }

            return $.ajax({
                url: url,
                type: submitMethod,
                dataType: type,
                data: data,
                success: callbackWithAjaxRedirect(callback)
            })
        }
    })

    $.each(['getJSON', 'postJSON', 'putJSON', 'deleteJSON', 'patchJSON'], function (i, method) {
        $[ method ] = function (url, data, callback) {
            if ($.isFunction(data)) {
                callback = data
                data = undefined
            }

            var submitMethod = method

            if (method.startsWith('put') || method.startsWith('patch') || method.startsWith('delete')) {
                var hasParam = url.contains('?')
                if (!hasParam) url = url + '?_method=' + method.replace('JSON', '')
                if (data) $.each(data, function(key, val) {
                    url = url + '&' + encodeURIComponent(key) + '=' + encodeURIComponent(val)
                })
                data = {}
                submitMethod = 'post'
            }
            if (method.startsWith('get')) {
                var hasParam = url.contains('?')
                if (!hasParam) {
                    url = url + '&now=' + new Date().getTime()
                } else {
                    url = url + '&now=' + new Date().getTime()
                }
            }
            return $.ajax({
                url: url,
                type: submitMethod.replace('JSON', ''),
                dataType: 'json',
                data: data,
                success: callbackWithAjaxRedirect(callback)
            })
        }
    })

}

export default class App {

    configure() {
        setUpAjaxInterceptor()
    }

}