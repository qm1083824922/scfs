// nodejs 中的path模块
var path = require('path');

module.exports = {
    // 入口文件，path.resolve()方法，可以结合我们给定的两个参数最后生成绝对路径，最终指向的就是我们的index.js文件
    entry: path.resolve(__dirname, '../app.js'),
    // 输出配置
    output: {
        // 输出路径是 myProject/dist/static
        path: path.resolve(__dirname, '../dist'),
        //编辑后的发布目录
        publicPath: 'static/',
        filename: '[name].js?v=[hash]',
        chunkFilename: '[id].[chunkhash].js'
    },
    resolve: {
        extensions: ['', '.js', '.vue']
    },

    // 服务器配置相关，自动刷新!
    devServer: {
        contentBase: './',
        historyApiFallback: true,
        hot: true,
        inline: true,
        grogress: true,
        host: '10.32.4.233',
        proxy: {
            //'/scf/*': 'http://10.40.6.237:8080'   //开发服务器
            //'/scf/*': 'http://10.32.4.236:8080'   //唐俊杰
            //'/scf/*': 'http://10.32.4.235:8080' //周培
            '/scf/*': 'http://10.32.4.241:8080' //游建平
            
        }
    },
    module: {
        
        loaders: [
            // 使用vue-loader 加载 .vue 结尾的文件
            {
                test: /\.vue$/, 
                loader: 'vue'   
            },
            {
                test: /\.js$/,
                loader: 'babel',
                exclude: /node_modules/
            },
            // 编译sass 编译css并自动添加css前缀
            {
                test: /\.scss$/,
                loader: 'style!css?sourceMap!postcss!sass?sourceMap'
            },
            // backgrouind:url() loader
            {
                test: /\.(png|gif|jpg)$/,
                loader: 'url?limit=8192'
            }
        ]
    },
    babel: {
        presets: ['es2015', 'stage-2'],
        plugins: ['transform-runtime']
    },
    vue: {
        loaders: {
            css: 'style!css!postcss',
        }
    },
    externals: {
        'vue': 'Vue'
    },
    resolve: {
        extensions: ['', '.js', '.vue', 'scss', 'sass'],
        alias: {
            comps: path.join(__dirname, '../components'),
            store: path.join(__dirname, '../store'),
        }
    }
}