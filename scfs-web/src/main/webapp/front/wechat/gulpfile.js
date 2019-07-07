//用到的插件
var gulp = require('gulp'),
    rename = require('gulp-rename'),
 	minifyCss = require('gulp-minify-css'),
    spriter = require('gulp-css-spriter'),
    less = require('gulp-less'),
    uglify = require('gulp-uglify'),
    //jshint = require('gulp-jshint'),
    watchPath = require('gulp-watch-path'),
    autoprefixer = require('gulp-autoprefixer'),
    notify = require('gulp-notify');
    browserSync = require('browser-sync');

//相对路径，结尾一定要加上 /
var baseUrl = '';

var lessUrl = baseUrl + 'less/',//未压缩的less目录
    cssUrl = baseUrl + 'css/',//压缩后的css目录
    jsUrl = baseUrl + 'js/',//未压缩的js目录
    minjsUrl = baseUrl + 'minjs/',//压缩后js目录    
    styleImg = baseUrl + 'images/styleimg/';//合并后的图片目录

var spriteName = 'sprite.png', //合并后的图片名称
    spriteUrl = '../images/styleimg/' + spriteName; //生成的css中图片路径

var reload = browserSync.reload;

// 监视文件改动并重新载入
gulp.task('serve', function() {
  browserSync({
    server: {
      baseDir: './'
    }
  });

  gulp.watch(['./**/*.html', './**/*.css', './*.js'], {cwd: './'}, reload);
});

//检查JS语法
gulp.task('jshint',function () {
    return gulp.src(jsUrl + '*.js')
    .pipe(jshint())
    .pipe(jshint.reporter('default'));
});

//自动编译LESS
gulp.task('watchLess', function() {
    console.log(lessUrl)
    gulp.watch(lessUrl + '*.less', function(event) {
        var paths = watchPath(event , lessUrl , cssUrl);        
        //引入修改的文件路径
        gulp.src([lessUrl + paths.srcFilename , '!'+lessUrl+'base/*.less'])
        //编译less
        .pipe(less())
        .on('error',function(e){
            console.dir(e);
        })
        //浏览器前缀
        .pipe(autoprefixer({
            browsers: ['last 2 versions','ie >= 9','Firefox >= 10','last 3 Safari versions'],//兼容那些版本浏览器
            cascade: true,
            remove:true 
        }))
        //压缩css      
        .pipe(minifyCss({
            compatibility: 'ie7'
        }))
        //输出到路径
        .pipe(gulp.dest(cssUrl));
        //执行完毕后输出日志
        console.log("修改的文件：" + paths.srcPath + ' has ' + event.type);
        console.log("输出后的文件：" + paths.distPath);
    });
});


//自动压缩JS
gulp.task('watchJs', function () {
    gulp.watch(jsUrl + '*.js', function(event) {
        var paths = watchPath(event , jsUrl , minjsUrl);
        gulp.src( jsUrl + paths.srcFilename )
        //rename文件名
        .pipe(rename({suffix: '.min'}))   
        //压缩
        .pipe(uglify())
        //输出到路径
        .pipe(gulp.dest(minjsUrl))
        //执行完毕后输出日志
        console.log("修改的文件：" + paths.srcPath + ' has ' + event.type);
        console.log("输出后的文件：" + paths.distDir + '.min.js');
    });
});


gulp.task("default",function(){
    gulp.start(["watchLess"]);
});