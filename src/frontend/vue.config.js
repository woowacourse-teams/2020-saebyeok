const path = require('path');

module.exports = {
  transpileDependencies: ['vuetify'],
  outputDir: path.resolve(__dirname, '../' + 'main/resources/static'),
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9000',
        ws: true,
        changeOrigin: true
        //pathRewrite: { '/api': '/' }
      }
    }
    // devServer: { index: 'login.html' }
  }
  // pages: {
  //   // signin: {
  //   //   entry: 'src/pages/signin/main.js'
  //   //   // template: 'public/signin.html',
  //   //   // filename: 'signin.html',
  //   //   // title: '로딩중',
  //   //   // chunks: ['chunk-vendors', 'chunk-common', 'signin']
  //   // },
  //   signin: 'src/pages/signin/main.js',
  //   index: 'src/main.js'
  // }
};
