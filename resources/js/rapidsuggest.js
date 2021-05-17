var qq;
var hint;
var r;

var results_ps = [];
var selected_pointer = -1;

(function (window, document) {

    function setUp(q, options) {
        // Add the parameters to Rapid Suggest URL
        // options.url = http://localhost:8080/api/rapidsuggest?n=20&g=movies,people&q=
        options.url += "?";
        if (options.number) { options.url += 'n=' + options.number + '&';  }
        if (options.groups) { options.url += 'g=' + encodeURIComponent(options.groups) + '&'; }
        options.url += '&q=';

        // Create Wrapper where we put two input elements:
        // one for user query, other for displaying hints to be tabbed
        var wrapper = createDomElement('div', {className: '_rs-wrap'});
        hint = createDomElement('input', {
            tabindex: -1, //can't be reached via keyboard navigation, only with JS code .focus()
            spellcheck: false,
            autocomplete: 'off',
            readonly: 'readonly',
            type: 'text',
            className: q.className + ' _rs-hint'
        });
        wrapper.appendChild(hint);

        q.classList.add('_rs-foreground'); // Make q the input element to be seen/by our users
        qq = q.cloneNode(true); // deep clone of q
        wrapper.appendChild(qq);

        // Create DOM element to store the autocomplete results, and append in Wrapper as well
        r = createDomElement('div', {className: '_rs-results'});
        wrapper.appendChild(r);

        // Place Weapper in DOM and remove q as we now work with qq
        q.parentElement.insertBefore(wrapper, q);
        q.parentNode.removeChild(q);

        function displayResults(results) {
            if (results.length === 0) {
                r.style.display = 'none';
                hint.value = ''; // no results, so hint should be empty so it cannot be tab-completed
                return;
            }

            var i, len;
            var ps = r.getElementsByTagName('p'); // getting current results that need to be removed
            for (i = ps.length - 1; i >=0; i--) {
                ps[i].remove();
            }

            r.style.display = 'block';

            results_ps = [];
            var p, a;
            var hint_candidate = null;
            var hint_candidates = [];
            var search_terms = terms.map(escapeRegExp);
            var re = new RegExp('\\b(' + search_terms.join('|') + ')(\\w+)\\b', 'gi'); // case insensitive

            var p_fragments = document.createDocumentFragment();
            for (i=0, len=results.length; i < len; i++) {
                var found;
                var matched;

                while ((found = re.exec(results[i][1])) !== null) {
                    matched = new RegExp('\\b' + escapeRegExp(found[0]) + '\\b', 'gi'); // case insensitive

                    hint_candidate = found[found.length - 1];
                    if (hint_candidate !== undefined && !matched.test(qq.value))  {
                        if (selected_pointer === i || (selected_pointer === -1 && i === 0)) {
                            hint_candidates.push(hint_candidate);
                        }
                    }
                }

                p = createDomElement('p');
                if (i === selected_pointer) {
                    p.classList.add('selected');
                }

                a = createDomElement('a', {
                    innerHTML: highlightText(results[i][1]),
                    href: results[i][0]
                });
                p.appendChild(a);
                p_fragments.appendChild(p);
                results_ps.push(p);
            }
            r.appendChild(p_fragments);

            if (hint_candidates.length && qq.value.charAt(qq.value.length - 1) !== ' ') {
                hint_candidate = hint_candidates[Math.max(0, selected_pointer) % hint_candidates.length];
                hint.value = qq.value + hint_candidate;
            } else {
                hint.value = '';
            }

        }

        // Handler whenever we type a char in input element
        function handler() {
            console.log("Handler called. q.value: '" + qq.value + "'");

            // Check if hint is still applicable, if not, reset it
            if (hint.value.length) {
                if (hint.value.indexOf(qq.value.trim()) === -1) {
                    // hint no longer contains the user query, so let's reset it
                    hint.value = qq.value;
                }
            }

            // Let's reset the pointer, as new character was typed
            selected_pointer = -1;

            // Let's make the request to Rapid Suggest service
            var req = new window.XMLHttpRequest();
            req.onreadystatechange = function () {
                if (req.readyState === 4) { // DONE, the operation is complete
                    if (req.status === 200) {
                        var response = JSON.parse(req.responseText);
                        terms = response.terms;
                        displayResults(response.results); // [ terms : [..], results : []]
                    } else {
                        if (req.status !== 0) {
                            console.warn(req.status, req.responseText);
                            r.style.display = 'none';
                        }
                    }
                }
            };

            var url = location.protocol + '//' + location.host + '/rapidsuggest' + '?q=';
            req.open('GET', url + encodeURIComponent(qq.value.trim()), true);
            req.send();
            console.log("Requested autocomplete for '" + qq.value.trim() + "'");
        }

        // Let's attach some event handlers
        attachHandler(qq, 'input', handler);
        attachHandler(qq, 'keydown', handleKeyEvent);
        attachHandler(qq, 'blur', handleBlur);
        attachHandler(qq, 'focus', handleFocus);
    }

    window.RapidSuggest = setUp;

})(window, document);
