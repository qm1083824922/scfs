var path = require('path');
var webpack = require('webpack');
// 引入基本配置
var config = require('./base.config');

config.output.publicPath = '/';

//本地编译时，不打包map文件
config.devtool = '#source-map';
config.noInfo = true;

config.plugins = [
    
    // 添加三个插件
    // new webpack.optimize.OccurenceOrderPlugin(),
    // new webpack.HotModuleReplacementPlugin(),
    // new webpack.NoErrorsPlugin(),
];

module.exports = config;