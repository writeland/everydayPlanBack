const $ = selector => document.querySelector(selector);
const today = () => { const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`; };
let entries = [], filter = 'today', editing = null, notified = new Set();
const reminderKey = 'daily-checkin-reminders-enabled';
let remindersEnabled = localStorage.getItem(reminderKey) !== 'false';
const dialog = $('#dialog'), form = $('#form');
function toast(message) { const el = $('#toast'); el.textContent = message; el.style.display = 'block'; clearTimeout(toast.timer); toast.timer = setTimeout(() => el.style.display = 'none', 3500); }
async function api(path, options = {}) {
  const response = await fetch('/api/entries' + path, { ...options, headers: { 'Content-Type': 'application/json', ...options.headers } });
  if (!response.ok) throw new Error(`请求失败（${response.status}）`);
  return response.status === 204 ? null : response.json();
}
async function refresh() { try { entries = await api(''); render(); checkReminders(); } catch (e) { toast('无法连接后端，请确认服务正在运行。'); } }
function render() {
  const day = today(), todays = entries.filter(e => e.date === day), completed = todays.filter(e => e.done).length;
  $('#total').textContent = todays.length; $('#completed').textContent = completed;
  const progress = todays.length ? Math.round(completed / todays.length * 100) : 0;
  $('#percent').textContent = progress + '%'; $('#bar').style.width = progress + '%';
  const visible = entries.filter(e => filter === 'today' ? e.date === day : filter === 'done' ? e.done : true);
  $('#heading').firstChild.textContent = ({today:'今日计划 ',all:'全部日程 ',done:'已完成 '})[filter];
  $('#count').textContent = visible.length;
  $('#listSubtitle').textContent = ({today:'按自己的节奏，开始今天吧。',all:'所有计划，都在这里。',done:'回头看看，你已经走了这么远。'})[filter];
  const list = $('#list'); list.replaceChildren(); $('#empty').hidden = visible.length > 0;
  $('#empty h3').textContent = filter === 'done' ? '还没有完成的日程' : filter === 'all' ? '还没有日程' : '给今天留下一点计划';
  $('#empty p').textContent = filter === 'done' ? '完成一件小事后，它会出现在这里。' : '写下你想完成的小事吧。';
  $('#emptyAdd').hidden = filter === 'done';
  for (const entry of visible) {
    const item = document.createElement('article'); item.className = 'item' + (entry.done ? ' done' : '');
    const check = document.createElement('input'); check.type = 'checkbox'; check.className = 'check'; check.checked = entry.done; check.setAttribute('aria-label', `标记${entry.title}为${entry.done ? '未完成' : '已完成'}`);
    check.addEventListener('change', async () => { try { await api('/' + entry.id, {method:'PUT',body:JSON.stringify({...entry,done:check.checked})}); await refresh(); } catch { check.checked = entry.done; toast('保存失败，请重试。'); } });
    const body = document.createElement('div'); body.className = 'item-body';
    const title = document.createElement('div'); title.className = 'item-title'; title.textContent = entry.title;
    const meta = document.createElement('div'); meta.className = 'meta'; meta.textContent = `◷ ${entry.date}${entry.reminderTime ? ' · ' + entry.reminderTime.slice(0,5) + ' 提醒' : ''}`;
    body.append(title,meta); if (entry.note) { const note = document.createElement('p'); note.className = 'note'; note.textContent = entry.note; body.append(note); }
    const actions = document.createElement('div'); actions.className = 'actions';
    const edit = document.createElement('button'); edit.textContent = '编辑'; edit.addEventListener('click', () => openForm(entry));
    const del = document.createElement('button'); del.textContent = '删除'; del.addEventListener('click', async () => { if (!confirm('确定删除这条日程吗？')) return; try { await api('/'+entry.id,{method:'DELETE'}); await refresh(); toast('已删除'); } catch { toast('删除失败，请重试。'); } });
    actions.append(edit,del); item.append(check,body,actions); list.append(item);
  }
}
function openForm(entry = null) { editing = entry; form.reset(); $('#modalTitle').textContent = entry ? '编辑日程' : '新建日程'; $('#title').value = entry?.title || ''; $('#date').value = entry?.date || today(); $('#time').value = entry?.reminderTime?.slice(0,5) || ''; $('#note').value = entry?.note || ''; dialog.showModal(); $('#title').focus(); }
$('#add').onclick = () => openForm(); $('#emptyAdd').onclick = () => openForm(); $('#close').onclick = $('#cancel').onclick = () => dialog.close();
form.onsubmit = async event => { event.preventDefault(); const title = $('#title').value.trim(); if (!title) { toast('请填写日程内容。'); return; } const data = {title, date:$('#date').value, reminderTime:$('#time').value || null, note:$('#note').value, done:editing?.done || false}; try { await api(editing ? '/'+editing.id : '', {method:editing ? 'PUT' : 'POST',body:JSON.stringify(data)}); dialog.close(); await refresh(); toast('日程已保存'); } catch { toast('保存失败，请检查内容并重试。'); } };
document.querySelectorAll('.nav').forEach(button => button.onclick = () => { filter = button.dataset.filter; document.querySelectorAll('.nav').forEach(b => b.classList.toggle('active',b === button)); render(); });
function updateReminderButton() { const enabled = remindersEnabled && 'Notification' in window && Notification.permission === 'granted'; $('#notify').textContent = enabled ? '♧ 关闭提醒' : '♧ 开启提醒'; $('#notify').setAttribute('aria-pressed', String(enabled)); }
$('#notify').onclick = async () => { if (!('Notification' in window)) return toast('当前浏览器不支持通知。'); if (remindersEnabled && Notification.permission === 'granted') { remindersEnabled = false; localStorage.setItem(reminderKey, 'false'); updateReminderButton(); return toast('提醒已关闭。'); } try { const permission = Notification.permission === 'granted' ? 'granted' : await Notification.requestPermission(); if (permission === 'granted') { remindersEnabled = true; localStorage.setItem(reminderKey, 'true'); updateReminderButton(); toast('提醒已开启，请保持页面打开。'); checkReminders(); } else { updateReminderButton(); toast('未获得通知权限，请在浏览器设置中开启。'); } } catch { toast('无法开启通知，请检查浏览器设置。'); } };
window.addEventListener('storage', event => { if (event.key === reminderKey) { remindersEnabled = event.newValue !== 'false'; updateReminderButton(); } });
function checkReminders() { if (!remindersEnabled || !('Notification' in window) || Notification.permission !== 'granted') return; const now = new Date(); for (const e of entries) { if (e.done || !e.reminderTime || e.date !== today()) continue; const time = e.reminderTime.slice(0,5), key = `${e.id}-${e.date}-${time}`; const due = new Date(`${e.date}T${time}:00`); if (now >= due && now - due < 60000 && !notified.has(key)) { try { new Notification('日程提醒', {body:e.title}); notified.add(key); } catch { toast('浏览器无法显示通知。'); } } } }
function updateDate() { $('#dateLabel').textContent = new Intl.DateTimeFormat('zh-CN',{year:'numeric',month:'long',day:'numeric',weekday:'long'}).format(new Date()); }
updateReminderButton(); updateDate(); refresh(); setInterval(() => { updateReminderButton(); updateDate(); render(); checkReminders(); }, 15000);
