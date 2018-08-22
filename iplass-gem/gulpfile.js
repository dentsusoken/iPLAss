var gulp = require("gulp");
var requireDir = require("require-dir");
var runSequence = require("run-sequence");

requireDir("./gulp/tasks", {recurse: true});

//package.json の 'script' から実行される
gulp.task("build", function() {
  runSequence("build_sass");
});

gulp.task("buildImageColor", function() {
runSequence("build_sassImageColor");
});