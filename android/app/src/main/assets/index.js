/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	var __weex_template__ = __webpack_require__(4)
	var __weex_style__ = __webpack_require__(5)
	var __weex_script__ = __webpack_require__(6)

	__weex_define__('@weex-component/af3133d79ccd018036a9c37506662ba2', [], function(__weex_require__, __weex_exports__, __weex_module__) {

	    __weex_script__(__weex_module__, __weex_exports__, __weex_require__)
	    if (__weex_exports__.__esModule && __weex_exports__.default) {
	      __weex_module__.exports = __weex_exports__.default
	    }

	    __weex_module__.exports.template = __weex_template__

	    __weex_module__.exports.style = __weex_style__

	})

	__weex_bootstrap__('@weex-component/af3133d79ccd018036a9c37506662ba2',undefined,undefined)

/***/ },
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */
/***/ function(module, exports) {

	module.exports = {
	  "type": "mydrawerlayout",
	  "events": {
	    "androidback": "back"
	  },
	  "children": [
	    {
	      "type": "mypageview",
	      "attr": {
	        "primarycolor": "#f44336"
	      },
	      "children": [
	        {
	          "type": "toolbar",
	          "classList": [
	            "toolbar"
	          ],
	          "children": [
	            {
	              "type": "text",
	              "classList": [
	                "header"
	              ],
	              "attr": {
	                "value": "Economic Times"
	              }
	            }
	          ]
	        },
	        {
	          "type": "mypageitem",
	          "repeat": function () {return this.pageItems},
	          "attr": {
	            "title": function () {return this.title}
	          },
	          "children": [
	            {
	              "type": "div",
	              "style": {
	                "flex": 1
	              },
	              "children": [
	                {
	                  "type": "embed",
	                  "classList": [
	                    "content"
	                  ],
	                  "style": {
	                    "visibility": "visible"
	                  },
	                  "attr": {
	                    "src": function () {return this.src},
	                    "type": "weex"
	                  }
	                }
	              ]
	            }
	          ]
	        }
	      ]
	    },
	    {
	      "type": "mydrawerview",
	      "attr": {
	        "alignment": "left"
	      },
	      "style": {
	        "flex": 1,
	        "backgroundColor": "#FFFFFF",
	        "position": "absolute",
	        "top": 0,
	        "bottom": 0
	      },
	      "children": [
	        {
	          "type": "div",
	          "style": {
	            "width": 750,
	            "height": 300,
	            "backgroundColor": "#111111"
	          },
	          "children": [
	            {
	              "type": "image",
	              "attr": {
	                "src": "http://takshashila.org.in/wp-content/uploads/2015/01/EconomicTimes1.png"
	              },
	              "style": {
	                "width": 750,
	                "height": 300,
	                "backgroundColor": "#FFFFFF"
	              }
	            }
	          ]
	        },
	        {
	          "type": "div",
	          "style": {
	            "flex": 1
	          },
	          "children": [
	            {
	              "type": "embed",
	              "id": "newsListElement",
	              "classList": [
	                "content"
	              ],
	              "style": {
	                "visibility": "visible"
	              },
	              "attr": {
	                "src": "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list.js",
	                "type": "weex"
	              }
	            },
	            {
	              "type": "text",
	              "attr": {
	                "value": "Hello"
	              }
	            }
	          ]
	        }
	      ]
	    }
	  ]
	}

/***/ },
/* 5 */
/***/ function(module, exports) {

	module.exports = {
	  "wrapper": {
	    "width": 750,
	    "position": "absolute",
	    "top": 0,
	    "left": 0,
	    "right": 0,
	    "bottom": 0
	  },
	  "content": {
	    "width": 750,
	    "flex": 1
	  },
	  "item": {
	    "justifyContent": "center",
	    "height": 100,
	    "padding": 20
	  },
	  "mytitle": {
	    "color": "#000000",
	    "fontSize": 50
	  },
	  "toolbar": {
	    "fontSize": 50,
	    "paddingTop": 20,
	    "flexDirection": "row",
	    "justifyContent": "center"
	  },
	  "header": {
	    "color": "#FFFFFF"
	  },
	  "flexRow": {
	    "flexDirection": "row"
	  },
	  "flexColumn": {
	    "flexDirection": "column"
	  },
	  "fixed": {
	    "position": "fixed",
	    "bottom": 50,
	    "left": 50,
	    "width": 300,
	    "height": 50,
	    "alignItems": "center",
	    "justifyContent": "center"
	  },
	  "shopImg": {
	    "width": 220,
	    "height": 220
	  },
	  "list": {
	    "flexDirection": "column",
	    "overflow": "hidden",
	    "width": 750,
	    "height": 1200,
	    "backgroundColor": "#dddddd"
	  },
	  "cell": {
	    "backgroundColor": "#dddddd",
	    "flexDirection": "column",
	    "width": 750
	  },
	  "shopDiv": {
	    "flexDirection": "column",
	    "backgroundColor": "#ffffff",
	    "margin": 5,
	    "padding": 10,
	    "borderWidth": 1,
	    "borderColor": "#cccccc",
	    "overflow": "visible"
	  },
	  "shopRowList": {
	    "flexDirection": "column",
	    "borderWidth": 1,
	    "borderColor": "#cccccc",
	    "overflow": "visible",
	    "margin": 5,
	    "padding": 10,
	    "backgroundColor": "#ffffff"
	  },
	  "shopHeader": {
	    "flexDirection": "row",
	    "width": 720
	  },
	  "shopFooter": {
	    "flexDirection": "row",
	    "width": 720
	  },
	  "smallImg": {
	    "width": 20,
	    "height": 20
	  },
	  "refresh-view": {
	    "height": 120,
	    "width": 750,
	    "display": "flex",
	    "MsFlexAlign": "center",
	    "WebkitAlignItems": "center",
	    "WebkitBoxAlign": "center",
	    "alignItems": "center"
	  }
	}

/***/ },
/* 6 */
/***/ function(module, exports) {

	module.exports = function(module, exports, __weex_require__){"use strict";

	module.exports = {

	  data: function () {return {
	    imageUrl: 'https://cdn.photographylife.com/wp-content/uploads/2012/01/Nikon-500mm-f4-Sample-4-960x638.jpg',
	    pageItems: [{
	      title: "EconomicTimes",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-et.js"
	    }, {
	      title: "MoneyControl",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-mc.js"
	    }, {
	      title: "WashingtonTimes",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-wt.js"
	    }, {
	      title: "MF",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-et.js"
	    }, {
	      title: "StartUps",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-mc.js"
	    }, {
	      title: "Videos",
	      src: "https://s3.ap-south-1.amazonaws.com/uploader-assets/sample-list-wt.js"
	    }]
	  }},
	  created: function created() {
	    var globalEvent = __weex_require__('@weex-module/globalEvent');
	    globalEvent.addEventListener("back", function (e) {
	      console.log("Global back event");
	    });
	  },
	  methods: {
	    back: function back() {
	      console.log("back button pressed!");
	      this.$broadcast('goBack', {
	        to: "Hello"
	      });
	    }
	  }
	};}
	/* generated by weex-loader */


/***/ }
/******/ ]);