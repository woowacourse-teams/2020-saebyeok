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
  },
  pages: {
    login: {
      entry: 'src/pages/login/main.js'
    },
    index: {
      entry: 'src/main.js'
    }
  }
};
