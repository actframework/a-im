import View from './view/app.view.js'
import App from './app.class.js'

const AppInstance = new App()

window.App = AppInstance

// window.onerror = function(a,b,c){
// 	alert(JSON.stringify({a:a,b:b,c:c}))
// }

document.addEventListener('DOMContentLoaded', ()=>{
    AppInstance.configure()
    AppInstance.view = new View()
    AppInstance.view.start()

    console.log( 'ENV:', process.env )

    console.log('')
})

// Enable SW in production
// https://github.com/facebookincubator/create-react-app/blob/master/packages/react-scripts/template/README.md#making-a-progressive-web-app
// serviceWorkerRegistration.register()
