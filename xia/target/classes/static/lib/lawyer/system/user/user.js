/**
 * Created by lindeng on 10/4/2016.
 */
define(['jquery','bootstrap','dataTableBoot','dataTableButton',
    '/lib/dataTables/datatables.net-buttons/js/buttons.flash.min.js',
    '/lib/dataTables/datatables.net-buttons/js/buttons.html5.min.js',
    '/lib/dataTables/datatables.net-fixedheader/js/dataTables.fixedHeader.min.js',
    'lib/dataTables/datatables.net-scroller/js/datatables.scroller.min.js'],
    function ($,bootstrap,datatable,button,flash,html5B,fixedHeader,scroller) {
    User=function () {
        return{
            init:function (tableId) {
                if($("#"+tableId+"").length){
                    $("#"+tableId+"").DataTable({
                        dom:"Bfrtip",
                        buttons:[
                            {
                                extend:"excel",
                                className:"btn-sm"
                            }
                        ]
                    });
                }
            }
        }
    }();
});

$(function () {
    User.init("datatable-buttons");
});
