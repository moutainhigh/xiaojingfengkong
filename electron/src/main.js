const electron = require('electron');
// Module to control application life.
const {app,Tray,Menu,globalShortcut} = electron;
// Module to create native browser window.
const {BrowserWindow} = electron;
const path = require('path');
const iconPath = path.join(__dirname, 'icon.png');

const template = [
 {
    label: '文件',
	role: 'File',
    submenu: [
      {label: '退出',role: 'quit',accelerator: 'Ctrl+q'}
    ]
  },
  {
    label: '编辑',
    submenu: [
      {label: '重做',role: 'undo'},
      {label: '全选',role: 'redo'},
      {type: 'separator'},
      {label: '剪切',role: 'cut'},
      {label: '复制',role: 'copy'},
      {label: '粘贴',role: 'paste'},
      {label: '删除',role: 'delete'},
      {label: '全选',role: 'selectall'}
    ]
  },
  {
    label: '视图',
    submenu: [
      {label: '刷新',role: 'reload'},
      {label: '强制刷新',role: 'forcereload'},
      {type: 'separator'},
      {label: '原始大小',role: 'resetzoom'},
      {label: '放大视图',role: 'zoomin',  accelerator: 'Ctrl+Up'},
      {label: '缩小视图',role: 'zoomout',  accelerator: 'Ctrl+Down'},
      {type: 'separator'},
      {label: '最小化',role: 'minimize'},
      {label: '全屏',role: 'togglefullscreen'},
      {label: '调试工具',role: 'toggledevtools',accelerator: 'F12'}
    ]
  },
  {
	label: '帮助',
	role: 'help',
    submenu: [
      {
        label: '官方网站',
        click () { require('electron').shell.openExternal('http://www.magicksoft.com') }
      }
    ]
  }
]

const menu = Menu.buildFromTemplate(template)

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let win;

function createWindow() {

  // Create the browser window.
  win = new BrowserWindow({minWidth:1200,minHeight:720,fullscreenable: true,frame:true,resizable:true,backgroundColor: '#F3F3F3'});
  // and load the index.html of the app.
  win.loadURL("http://localhost");
  win.maximize();

  // Open the DevTools.
  //win.webContents.openDevTools();

  //globalShortcut.register('F12', () => {
	//win.show();
    //win.webContents.openDevTools();
  //})


  tray = new Tray(iconPath);
  var contextMenu = Menu.buildFromTemplate([
    {
      label: 'Open DAS',
      icon: iconPath,
	  click: function() {
        win.show();
      }
    },
    {
      label: 'Hide DAS',
	  click: function() {
        win.hide();
      }
    },
    { label: 'Quit',
      click: function() {
        win.close();
      }
    }
  ]);
  tray.on('click', () => {
	win.isVisible() ? win.hide() : win.show()
  })

  tray.setToolTip('Das is running.');
  tray.setContextMenu(contextMenu);


	win.on('show', () => {
	  tray.setHighlightMode('always')
	});
			
	win.on('hide', () => {
	  tray.setHighlightMode('never')
	});

  // Emitted when the window is closed.
  win.on('closed', () => {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    win = null;
  });
	
  //Menu.setApplicationMenu(menu);
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow);

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On OS X it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
	globalShortcut.unregisterAll()
    app.quit();
  }
});
app.on('browser-window-created',function(e,window) {
      //window.setMenu(null);
  });
app.on('activate', () => {
  // On OS X it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (win === null) {
    createWindow();
  }
});

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.