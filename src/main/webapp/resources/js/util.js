function createDomElement(tag, options) {
    var e = document.createElement(tag);
    for (var key in options) {
      e[key] = options[key];
    }
    return e;
}

// Utility function for generic event attachment
function attachHandler(target, type, handler) {
    if (target.addEventListener) {
      target.addEventListener(type, handler, false);
    } else {
      target.attachEvent(type, handler);
    }
  }

// Event handlers
function handleBlur(event) {
  hint.value = qq.value;
  // Necessary so it becomes possible to click the links before they disappear too quickly.
  setTimeout(function() {  r.style.display = 'none'; }, 200);
  event.preventDefault();
}

function handleFocus() {
  if (qq.value.length && results_ps.length) {
    r.style.display = 'block';
  }
}

function handleKeyEvent(e) {
  console.log('Key event: ' + e.keyCode);
  var relevant_keycodes = {
    13: 'enter',
    9: 'tab',
    38: 'up',
    40: 'down',
    27: 'esc'
  };
  // if not a relevant key, we do nothing here, default behavior is done
  if (!relevant_keycodes[e.keyCode])
    return false;

  e.preventDefault();
  return handleKeyboardEvent(relevant_keycodes[e.keyCode]);
}

function handleKeyboardEvent(name) {
  console.log('Keyboard event: ' + name);
  console.log("q.value : '" + qq.value + "'");
  console.log("hint.value : '" + hint.value + "'");
  var i, len;
  if (name === 'tab') {

    if (hint.value !== '' && hint.value !== qq.value) {
      qq.value = hint.value + ' ';
    }

  } else if (name === 'down' || name === 'up') {
    console.log('Pressed ' + name + '. index is ' + selected_pointer);

    if (name === 'down') {
      selected_pointer = Math.min(results_ps.length - 1, ++selected_pointer);
    } else if (name === 'up') {
      selected_pointer = Math.max(0, --selected_pointer);
    }
    for (i=0, len=results_ps.length; i < len; i++) {
      if (i === selected_pointer) {
        results_ps[i].classList.add('selected');
      } else {
        results_ps[i].classList.remove('selected');
      }
    }

  } else if (name === 'enter') {
    if (results_ps.length) {
      var p = results_ps[Math.max(0, selected_pointer)];
      var a = p.getElementsByTagName('a')[0];
      qq.value = hint.value = a.textContent;
      r.style.display = 'none';
      window.location = a.href;
    }
  } else if (name === 'esc') {
    r.style.display = 'none';
  }
  return false;
}

// Better visual display for user experience
function escapeRegExp(str) {
  return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
}

function highlightText(text) {
  var search_terms = terms.map(escapeRegExp);
  var re = new RegExp('\\b((' + search_terms.join('|') + ')[\\S\']*)', 'gi');
  return text.replace(re, '<b>$1</b>');
}
