/*global module:false*/

var md5File = require('md5-file'),
    fs = require('fs');

var getFileToken = function (filePath) {

    if (!fs.existsSync(filePath)) {
        return '' + +new Date();
    }

    var md5 = md5File(filePath);
    var result = [md5.slice(0, 16), md5.slice(16)];

    var f = '';
    result.forEach(function (item) {
        var num = (parseInt(item, 16) % 997).toString();
        while (num.length < 3) {
            num = '0' + num;
        }
        f += num;
    });
    return f;

};

module.exports = function (grunt) {

    var requirejsConfig = {
        // 合并所有css文件
        mainCss: {
            options: {
                optimizeCss: 'standard', // 标准方式，移除所有空格、注释等
                // optimizeCss: 'standard.keepLines', // 类似标准方式，但是保留换行
                // optimizeCss: 'standard.keepComments', // 保留注释，但是移除换行
                // optimizeCss: 'standard.keepComments.keepLines', // 保留注释，保留换行
                // optimizeCss: 'standard.keepWhitespace', // 保留不必要的空格
                cssIn: 'src/css/main.css',
                out: 'build/src/css/main.css'
            }
        },
        // 合并首页css文件
        indexCss: {
            options: {
                optimizeCss: 'standard', // 标准方式，移除所有空格、注释等
                // optimizeCss: 'standard.keepLines', // 类似标准方式，但是保留换行
                // optimizeCss: 'standard.keepComments', // 保留注释，但是移除换行
                // optimizeCss: 'standard.keepComments.keepLines', // 保留注释，保留换行
                // optimizeCss: 'standard.keepWhitespace', // 保留不必要的空格
                cssIn: 'src/css/index.css',
                out: 'build/src/css/index.css'
            }
        }
    };



    var module = {
        'main':'main',
        'indexzcmain':'indexzcmain',
        'index':'index',
        'ssq':'ssq',
        'dlt':'dlt',
        'qxc':'qxc',
        'qlc':'qlc',
        'k3':'k3',
        'zc':'zc',
        'ucenter':'ucenter',
        'award':'calcu',
        'goucai':'goucai',
        'login':'login',
        'help':'help',
        'common':'common',
        'jczq':'jczq',
    }


    for (var key in module){
        requirejsConfig[key] = {
            options: {
                baseUrl: 'src/js/', // js路径
                name: key, // 主模块名称
                out: 'build/src/js/'+ module[key] +'.js' // 输出文件路径
            }
        }
    }
    
    // Project configuration.
    grunt.initConfig({
            // Metadata.
            pkg: grunt.file.readJSON('package.json'),
            banner: '',
            requirejs: requirejsConfig,
            // 给css文件添加时banner
            cssmin: {
                build: {
                    options: {
                        banner: '<%= banner %>',
                    },
                    files: {
                        'build/css/main.css': ['build/css/main.css']
                    }
                }
            },
            // copy文件
            copy: {
                img: {
                    files: [
                        {
                            expand: true,
                            cwd: 'src/',
                            src: ['img/**'],
                            dest: 'build/src/'
                        }
                    ]
                },
				html: {
                    files: [
                        {
                            expand: true,
                            cwd: './',
                            src: ['*.html'],
                            dest: 'build/'
                        }
                    ]
                },
                htm: {
                    files: [
                        {
                            expand: true,
                            cwd: 'src/htm',
                            src: ['*.htm'],
                            dest: 'build/src/htm'
                        }
                    ]
                },
				js: {
                    files: [
                        {
                            expand: true,
                            cwd: 'src/js/',
                            src: ['base.js'],
                            dest: 'build/src/js/'
                        }
                    ]
                }
            },
            // 替换css文件里的图片地址加时间戳
            replace: {
                build: {
                    src: ['build/css/*.css'],
                    overwrite: true, // overwrite matched source files
                    replacements: [
                        {
                            from: /url\(['"]?(.*?)['"]?\)/g,
                            to: function (matchedWord, index, fullText, regexMatches) {

                                var imgPath = regexMatches[0];
                                var fullPath = 'build/css/' + imgPath;

                                return matchedWord.replace(imgPath, imgPath + '?t=' + getFileToken(fullPath));
                            }
                        }
                    ]
                }
            },
            concat: {
                dist: {
                    options: {
                        banner: ''
                    },
                    src: [
                        'src/js/base/jquery-1.10.2.min.js',
                        'src/js/base/underscore-min.js',
                        'src/js/base/backbone-min.js',
                        'src/js/base/require.js'
                    ],
                    dest: 'src/js/base.js'
                }
            },
            connect: {
                server: {
                    options: {
                        port: 8889  //新分支端口
                    }
                }
            },
            watch: {
                files: ['*.html',"*/*.js"],
                tasks: ['connect']
            }
        }
    )
    ;

    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-text-replace');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-watch');


// Default task.
// grunt.registerTask('default', ['requirejs', 'cssmin', 'copy', 'replace']);
    grunt.registerTask('default', ['requirejs', 'copy', 'replace']);

    grunt.registerTask('base', ['concat']);
    grunt.registerTask('server', ['connect', 'watch' ]);
}
;