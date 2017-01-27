$(document).ready(function() {
    $('#pinBoot').pinterest_grid({
        no_columns: 5,
        padding_x: 10,
        padding_y: 10,
        margin_bottom: 50,
        single_column_breakpoint: 700
    });

    var $container	= $('#pinBoot'),
        $articles	= $container.children('.white-panel'),
        timeout;

    $articles.on( 'mouseenter', function( event ) {

        var $article	= $(this);
        clearTimeout( timeout );
        timeout = setTimeout( function() {

            if( $article.hasClass('active') ) return false;

            $articles.not($article).removeClass('active').addClass('blur');

            $article.removeClass('blur').addClass('active');

        }, 75 );

    });

    $container.on( 'mouseleave', function( event ) {

        clearTimeout( timeout );
        $articles.removeClass('active blur');

    });

    var mainbottom = $('#main').offset().top + $('#main').height();

    $(window).on('scroll',function(){

        stop = Math.round($(window).scrollTop());
        if (stop > mainbottom) {
            $('.navbar-light').addClass('past-main');
            // $('.main-navigation .navbar-nav > li a').addClass('past-main')
        } else {
            $('.navbar-light').removeClass('past-main');
            // $('.main-navigation .navbar-nav > li a').removeClass('past-main')

        }

    });

    var dateFormat = function () {
        var dateElements = document.getElementsByClassName('date');
        var formattedTime = "Crap";

        for (var i = 0; i < dateElements.length; i++){

            formattedTime = moment(dateElements[i].innerText, 'yyyy-M-D hh:mm:ss').fromNow();

            dateElements[i].innerText = formattedTime;
        }
    };
    dateFormat();
});

(function ($, window, document, undefined) {
    var pluginName = 'pinterest_grid',
        defaults = {
            padding_x: 10,
            padding_y: 10,
            no_columns: 3,
            margin_bottom: 50,
            single_column_breakpoint: 700
        },
        columns,
        $article,
        article_width;

    function Plugin(element, options) {
        this.element = element;
        this.options = $.extend({}, defaults, options) ;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    Plugin.prototype.init = function () {
        var self = this,
            resize_finish;

        $(window).resize(function() {
            clearTimeout(resize_finish);
            resize_finish = setTimeout( function () {
                self.make_layout_change(self);
            }, 11);
        });

        self.make_layout_change(self);

        setTimeout(function() {
            $(window).resize();
        }, 500);
    };

    Plugin.prototype.calculate = function (single_column_mode) {
        var self = this,
            tallest = 0,
            row = 0,
            $container = $(this.element),
            container_width = $container.width();
        $article = $(this.element).children();

        if(single_column_mode === true) {
            article_width = $container.width() - self.options.padding_x;
        } else {
            article_width = ($container.width() - self.options.padding_x * self.options.no_columns) / self.options.no_columns;
        }

        $article.each(function() {
            $(this).css('width', article_width);
        });

        columns = self.options.no_columns;

        $article.each(function(index) {
            var current_column,
                left_out = 0,
                top = 0,
                $this = $(this),
                prevAll = $this.prevAll(),
                tallest = 0;

            if(single_column_mode === false) {
                current_column = (index % columns);
            } else {
                current_column = 0;
            }

            for(var t = 0; t < columns; t++) {
                $this.removeClass('c');
            }

            if(index % columns === 0) {
                row++;
            }

            $this.addClass('c' + current_column);
            $this.addClass('r' + row);

            prevAll.each(function(index) {
                if($(this).hasClass('c' + current_column)) {
                    top += $(this).outerHeight() + self.options.padding_y;
                }
            });

            if(single_column_mode === true) {
                left_out = 0;
            } else {
                left_out = (index % columns) * (article_width + self.options.padding_x);
            }

            $this.css({
                'left': left_out,
                'top' : top
            });
        });

        this.tallest($container);
        $(window).resize();
    };

    Plugin.prototype.tallest = function (_container) {
        var column_heights = [],
            largest = 0;

        for(var z = 0; z < columns; z++) {
            var temp_height = 0;
            _container.find('.c'+z).each(function() {
                temp_height += $(this).outerHeight();
            });
            column_heights[z] = temp_height;
        }

        largest = Math.max.apply(Math, column_heights);
        _container.css('height', largest + (this.options.padding_y + this.options.margin_bottom));
    };

    Plugin.prototype.make_layout_change = function (_self) {
        if($(window).width() < _self.options.single_column_breakpoint) {
            _self.calculate(true);
        } else {
            _self.calculate(false);
        }
    };

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName,
                    new Plugin(this, options));
            }
        });
    }

})(jQuery, window, document);

function build_coins(book) {
    var coins = "ctx_ver=Z39.88-2004";

    function add_coin(key, value) {
        coins += "&amp;" + key + "=" + encodeURIComponent(value);
    }

    add_coin("rft_val_fmt", "info:ofi/fmt:kev:mtx:book");
    add_coin("rfr_id", "info:sid/openlibrary.org:openbook");
    add_coin("rft.genre", "book");

    if (book.title)
        add_coin("rft.btitle", book.title);

    for (var i in book.authors) {
        add_coin("rtf.au", book.authors[i].name);
    }

    for (var i in book.publishers) {
        add_coin("rft.pub", book.publishers[i]);
    }

    return '<span class="Z3988" title="TITLE">'.replace("TITLE", coins);
}

/**
 * Add oln, full_title, alternate_authors and tooltip to book.
 */
function process_book(bibkey, book) {
    // delete empty values
    for (var k in book) {
        if (book[k] && book[k].length == 0)
            delete book[k];
    }

    // oln
    book.oln = book.key.replace('/b/', '');

    if (book.title_prefix)
        book.title = book.title_prefix + ' ' + book.title;

    // full_title
    if (book.subtitle)
        book.full_title = book.title + ": " + book.subtitle;
    else
        book.full_title = book.title;

    // alternate_authors
    if (book.authors)
        book.alternate_authors = null;
    else if (book.by_statement)
        book.alternate_authors = book.by_statement;
    else if (book.contributions)
        book.alternate_authors = book.contributions.join(", ");
    else
        book.alternate_authors = null;

    // publisher
    if (book.publishers)
        book.publisher = book.publishers[0];

    // isbn
    var isbn = bibkey;
    if (isbn && /^\d+$/.test(isbn) && (isbn.length == 10 || isbn.length == 13))
        book.isbn = isbn;
    else if (book.isbn_10)
        book.isbn = book.isbn_10[0];
    else if (book.isbn_13)
        book.isbn = book.isbn_13[0];

    // tooltip
    book.tooltip = "";

    if (book.first_sentence && book.first_sentence.value)
        book.tooltip += "First Sentence: " + book.first_sentence.value + " ";
    if (book.description && book.description.value)
        book.tooltip += "Description: " + book.description.value + " ";
    if (book.notes && book.notes.value)
        book.tooltip += "Notes: " + book.notes.value + " ";

    // if (book.tooltip == "")
    //     book.tooltip = "Click to view title in Open Library"
}

function setup_openbook(bibkey, book) {
    var e = $(".openbook[booknumber=N]".replace("N", bibkey));

    var v = $(".view[booknumber=N]".replace("N", bibkey));

    process_book(bibkey, book);

    function make_title() {
        var out = '<a class="openbook-title" target="_blank" title="Click to view title in Open Library" href="http://openlibrary.org/OLN">TITLE</a>';
        return out.replace("OLN", book.oln).replace("TITLE", book.full_title);
    }

    function make_author() {
        if (book.authors) {
            var t = '<a class="openbook-author" target="_blank" href="http://openlibrary.orgKEY" title="Click to view author in Open Library">NAME</a>';
            var out = "";
            for (var i in book.authors) {
                var a = book.authors[i];
                out += t.replace("KEY", a.key).replace("NAME", a.name);
            }
        }
        else if (book.alternate_authors) {
            out = "" + book.alternate_authors;
        }
        else
            out = "";
        return '<span class="openbook-authors">' + out + '</span>';
    }

    function make_description() {
        if (book.tooltip)
            return '<span class="openbook-publisher">' + book.tooltip + '</span>';
        else
            return "";
    }

    function make_worldcat() {
        var out = ''
            + '<a target="_blank" href="http://worldcat.org/isbn/ISBN" title="Find this title in a local library using WorldCat">'
            + 'Find in library  '
            + '</a>';
        return '<span>' + out.replace("ISBN", book.isbn) + '</span>';
    }

    function make_retail() {
        var out = ''
            + '<a target="_blank" href="https://www.amazon.com/s/keywords=ISBN" title="Find this title online to buy">'
            + 'Find in store'
            + '</a>';
        return '<span class="find">' + out.replace("ISBN", book.isbn) + '</span>';
    }

    var html = '<div class="openbook">'
        + '<h3>'
        + make_title()
        + '</h3>'
        + '<h4>'
        + make_author()
        + '</h4>'
        + build_coins(book)
        + '</div>';

    e.html(html);

    var view = '<div class="view">'
        + '<h3>'
        + make_title()
        + '</h3>'
        + '<h4>'
        + make_author()
        + '</h4>'
        + '<p>'
        + make_description()
        + '</p>'
        + '<span class="pull-right option">'
        + make_worldcat()
        + make_retail()
        + '</span>'
        + build_coins(book)
        + '</div>';

    v.html(view);
}

jQuery(document).ready(function() {
    var bibkeys = $.map($(".openbook, .view"), function(div) { return $(div).attr("booknumber"); });

    $.getJSON("http://openlibrary.org/api/books?bibkeys=" + bibkeys.join(",") + "&details=true&callback=?", function(data){
        for (var k in data)
            setup_openbook(k, data[k].details);
    });
});




