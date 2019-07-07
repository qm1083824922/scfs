var gulp       = require('gulp');
var svgSymbols = require('gulp-svg-symbols');
 
gulp.task('sprites', function () {
  return gulp.src('../images/svg/*.svg')
    .pipe(svgSymbols())
    .pipe(gulp.dest('../images/sprite'));
});