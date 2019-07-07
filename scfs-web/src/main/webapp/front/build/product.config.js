var path = require('path');
var webpack = require('webpack');
// 引入基本配置
var config = require('./base.config');

config.output.publicPath = 'dist/';

config.plugins = [
    
    // 添加三个插件
    // new webpack.optimize.OccurenceOrderPlugin(),
    // new webpack.HotModuleReplacementPlugin(),
    // new webpack.NoErrorsPlugin(),
];

module.exports = config;