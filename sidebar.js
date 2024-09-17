const fs = require('fs');
const path = require('path');

// 目录路径，假设所有的文档都存储在这个目录下
const docsDir = './docs';

// 递归函数用于构建侧边栏结构
function buildSidebar(dir, base) {
    const items = [];
    const files = fs.readdirSync(dir);

    files.forEach(file => {
        const filePath = path.join(dir, file);
        const stats = fs.statSync(filePath);
        let relativePath = path.relative(docsDir, filePath).replace(/\\/g, '/'); // 处理 Windows 路径

        // 忽略以 _ 或 . 开头的文件或目录
        if (file.startsWith('_') || file.startsWith('.') || file.startsWith('README')) {
            return;
        }

        if (stats.isDirectory()) {
            // 如果是目录，则递归调用 buildSidebar
            // 将目录名称中的空格替换为 %20
            relativePath = encodeURIComponent(relativePath);
            items.push(`- [${file}](#${relativePath}/)`); // 假设每个子目录都是一个章节
            items.push(...buildSidebar(filePath, relativePath));
        } else if (stats.isFile() && path.extname(file) === '.md') {
            // 如果是 .md 文件，则直接添加到侧边栏
            // 将文件名中的空格替换为 %20
            relativePath = encodeURIComponent(relativePath);

            // 获取当前文件所在的目录名
            const dirName = path.basename(path.dirname(filePath));

            // 如果文件名与所在目录名相同，则设置为一级目录
            if (file.replace('.md', '') === dirName) {
                items.push(`- [${file.replace('.md', '')}](${relativePath})`);
            } else {
                // 否则设置为二级目录
                items.push(`  - [${file.replace('.md', '')}](${relativePath})`);
            }
        }
    });

    return items;
}

// 构建侧边栏结构
const sidebarItems = buildSidebar(docsDir, '');

// 将侧边栏结构转换为字符串
let sidebarContent = '# 侧边栏\n' + sidebarItems.join('\n');

// 写入 _sidebar.md 文件
fs.writeFile('_sidebar.md', sidebarContent, (err) => {
    if (err) throw err;
    console.log('_sidebar.md 文件已成功生成！');
});