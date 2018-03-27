var merge = require("webpack-merge");

var generatedConfig = require('./scalajs.webpack.config');
var commonConfig = require("./common.webpack.config.js");

// Exported modules
var globalModules = {
  "material-ui": "mui",
  "material-ui/styles" : "mui.Styles",
  "material-ui/svg-icons/index" : "mui.SvgIcons",
  "react": "React"
};

Object.keys(generatedConfig.entry).forEach(function(key) {
  // Prepend each entry with the globally exposed JS dependencies
  generatedConfig.entry[key] = Object.keys(globalModules).concat(generatedConfig.entry[key]);
});

// Globally expose the JS dependencies
commonConfig.module.loaders = Object.keys(globalModules).map(function (pkg) {
  return {
    test: require.resolve(pkg),
    loader: "expose-loader?" + globalModules[pkg]
  }
});

module.exports = merge(generatedConfig, commonConfig);
