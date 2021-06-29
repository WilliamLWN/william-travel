$(function () {
    //过1秒后再加载，vue渲染jq选择器不起作用，等全部加载完成再调用
    setTimeout(playAni, 100);
});


function playAni() {
    //焦点图效果
    //点击图片切换图片
    $('.little_img').on('mousemove', function () {
        $('.little_img').removeClass('cur_img');
        var big_pic = $(this).data('bigpic');
        $('.big_img').attr('src', big_pic);
        $(this).addClass('cur_img');
    });
    //上下切换
    var picindex = 0;
    var nextindex = 4;
    //向下箭头
    $('.down_img').on('click', function () {
        var num = $('.little_img').length;
        if ((nextindex + 1) <= num) {
            $('.little_img:eq(' + picindex + ')').hide();
            $('.little_img:eq(' + nextindex + ')').show();
            picindex = picindex + 1;
            nextindex = nextindex + 1;
        }
    });
    //向上箭头
    $('.up_img').on('click', function () {
        var num = $('.little_img').length;
        if (picindex > 0) {
            $('.little_img:eq(' + (nextindex - 1) + ')').hide();
            $('.little_img:eq(' + (picindex - 1) + ')').show();
            picindex = picindex - 1;
            nextindex = nextindex - 1;
        }
    });
    //自动播放
    setInterval("auto_play()", 5000);
}

//自动轮播方法
function auto_play() {
    var cur_index = $('.prosum_left dd').find('a.cur_img').index();
    cur_index = cur_index - 1;
    var num = $('.little_img').length;
    var max_index = 3;
    if ((num - 1) < 3) {
        max_index = num - 1;
    }
    if (cur_index < max_index) {
        var next_index = cur_index + 1;
        var big_pic = $('.little_img:eq(' + next_index + ')').data('bigpic');
        $('.little_img').removeClass('cur_img');
        $('.little_img:eq(' + next_index + ')').addClass('cur_img');
        $('.big_img').attr('src', big_pic);
    } else {
        var big_pic = $('.little_img:eq(0)').data('bigpic');
        $('.little_img').removeClass('cur_img');
        $('.little_img:eq(0)').addClass('cur_img');
        $('.big_img').attr('src', big_pic);
    }
}