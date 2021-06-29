/**
 * @author NewBoy Modified
 * @since 2020-12
 */
Vue.component('zpageNav', {
	template: `<nav class="zpagenav">` +
	    `<div class="zpagetotal">每页{{pageSize}}条 共{{pageCount}}页 {{total}}条</div>` +
		`<ul class="page-ul">` +
		`<li v-bind:key="index" v-for="(item,index) in pageList" v-bind:class ="item.class" @click.stop="setPage(item)" v-html="item.html">` +
		`</li>` +
		`</ul>` +
		`</nav>`,
	props: {
		prevHtml: String,
		nextHtml: String,
		page: Number, //当前页
		total: Number,  //总条数
		pageSize: Number,  //页面大小
		maxPage: Number
	},
	computed: {
	    //总页数
        pageCount:function() {
            return Math.ceil(this.total / this.pageSize);
        },
	    //页码数组
		pageList: function() {
			var _this = this,
				pageList = [];
			let pageCount = Math.ceil(_this.total / _this.pageSize);
			let page = _this.page;
			let prevHtml = _this.prevHtml ? _this.prevHtml : '上一页';
			let nextHtml = _this.nextHtml ? _this.nextHtml : '下一页';
			let maxPage = _this.maxPage ? _this.maxPage : 9;  //默认是最下面显示9个页码

			let hasPrev = page > 1;
			let hasNext = page < pageCount;

			//上一页
			pageList.push({
				class: hasPrev ? '' : 'disabled',
				page: hasPrev ? page - 1 : page,
				html: prevHtml
			});

            //下一页
            pageList.push({
                class: hasNext ? '' : 'disabled',
                page: hasNext ? page + 1 : page,
                html: nextHtml
            });

			//首页
			pageList.push({
				class: page == 1 ? 'active' : '',
				page: 1,
				html: 1
			});

			var p0 = Math.floor(maxPage / 2);
			var p1 = 1 + 2 + p0; //首页+省略至少2个页码+中间页面数的一半

			var start, end;
			if(page >= p1) {
				start = page - p0;
				//前置省略号
				pageList.push({
					class: 'dot',
					page: page,
					html: '...'
				});
			} else {
				start = 2;
			}

			var p2 = page + p0;
			if(p2 < pageCount) {
				end = p2;
			} else {
				end = pageCount - 1;
			}

			//页码列表
			for(let i = start; i <= end; i++) {
				pageList.push({
					class: page == i ? 'active' : '',
					page: i,
					html: i
				});
			}

			if(end < pageCount - 1) {
				//后置省略号
				pageList.push({
					class: 'dot',
					page: page,
					html: '...'
				});
			}

			//尾页
			if(pageCount > 1) {
				pageList.push({
					class: page == pageCount ? 'active' : '',
					page: pageCount,
					html: pageCount
				});
			}

			return pageList;
		}
	},
	methods: {
		setPage: function(item) {
			if(item.class == '') {
				this.$emit('pagehandler', item.page);
			}
		}
	}
});