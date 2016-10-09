/**
 * Created by lindeng on 10/1/2016.
 */
require.config({
    baseUrl:"",
    paths : {
        // "html5shiv" : ["https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"],
        // "respond" : ["https://oss.maxcdn.com/respond/1.4.2/respond.min.js"],
        jquery:"/lib/plugins/jQuery/jquery-2.2.3.min",
        bootstrap:"/lib/bootstrap/js/bootstrap.min",
        app:"/lib/dist/js/app.min",
        dataTable:"/lib/dataTables/datatables.net/js/jquery.dataTables.min.js",
        dataTableBoot:"/lib/datatables.net-bs/js/dataTables.bootstrap.min.js",
        dataTableButton:"/lib/datatables.net-buttons/js/dataTables.buttons.min.js",
    },
    shim : {
        bootstrap : {
            deps : [ 'jquery' ],
            exports : 'bootstrap'
        },
        app:{
            deps : [ 'jquery' ],
            exports:'app'
        },
        dataTable:{
            deps:['jquery'],
            exports:'dataTable'
        },
        dataTableBoot:{
            deps:['jquery','bootstrap','dataTable'],
            exports:'dataTableBoot'
        },
        dataTableButton:{
            deps:['jquery','bootstrap','dataTable'],
            exports:'dataTableButton'
        }
    }
});
require(["jquery", "bootstrap","app"], function($, bootstrap,app) {
        //return an object to define the "my/shirt" module.
        Menu.select();
    }
);

define(["jquery"],function ($) {
    Menu=function () {
        return{
            select:function () {
                $(".treeview .treeview-menu li a").each(function () {
                    $(this).unbind("click").bind("click",function (e) {
                        e.preventDefault();
                        alert($(this).attr("href"));
                        $(".content").load($(this).attr("href"));
                    });
                });
            }
        };
    }();
});