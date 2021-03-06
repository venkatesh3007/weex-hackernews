var path = require('path')

module.exports = {
  entry: {
    'index': path.resolve('src', 'entry.js')
  },
  output: {
    path: 'dist',
    filename: '[name].web.js'
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        loaders: ['babel-loader'],
        exclude: /node_modules/
      }, {
        test: /\.vue(\?[^?]+)?$/,
        loaders: ['vue-loader']
      }
    ]
  }
}
