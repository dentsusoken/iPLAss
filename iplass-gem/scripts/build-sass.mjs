import { compile } from 'sass';
import fs from 'fs';

// sass ソースコードディレクトリ
const SRC_SCSS_DIR='src/main/sass';
// css 出力先
const DEST_SKIN_DIR='src/main/resources/META-INF/resources/styles/gem/skin';
// errorhtml css 出力先
const DEST_ERROR_HTML_DIR='src/main/resources/META-INF/resources/errorhtml/styles';
// skin 種類
const SKIN_TYPE = [ 'flat', 'horizontal', 'vertical' ];
// イメージカラー種類（src/main/sass/{type}/structure/imagecolor/{imageColor}.scss）
const IMAGE_COLOR_TYPE = [ 'blue', 'green', 'yellow', 'red' ];
// テーマ種類（src/main/sass/{type}/theme/{theme}.scss）
const THEME_TYPE = [ 'black', 'blue', 'green', 'lightblue', 'lightgreen', 'lightred', 'orange', 'red' ];

// sass コンパイルオプション
const compileOption = {
    sourceMap: false,
};

// arguments
const isImageColorOnly = process.argv.includes('--image-color-only');

const compileSass = (src, dest) => {
    console.log('----------');
    console.log(`Compile ${src} to ${dest}.`);
    console.log('----------');
    fs.rmSync(dest, { force: true });
    const result = compile(src, compileOption);
    fs.writeFileSync(dest, result.css);
    console.log('done.');
};

const doBase = (type) => {
    // base, clear, module, structure をコンパイル
    compileSass(`${SRC_SCSS_DIR}/${type}/base/base.scss`, `${DEST_SKIN_DIR}/${type}/base.css`);
    compileSass(`${SRC_SCSS_DIR}/${type}/clear/clear.scss`, `${DEST_SKIN_DIR}/${type}/clear.css`);
    compileSass(`${SRC_SCSS_DIR}/${type}/module/module.scss`, `${DEST_SKIN_DIR}/${type}/module.css`);
    compileSass(`${SRC_SCSS_DIR}/${type}/structure/structure.scss`, `${DEST_SKIN_DIR}/${type}/structure.css`);
};

const doImageColor = (type) => {
    IMAGE_COLOR_TYPE.forEach(imageColor => {
        // styles/gem/skin/{type}/imagecolor/{imageColor}.css に出力
        compileSass(`${SRC_SCSS_DIR}/${type}/structure/imagecolor/${imageColor}.scss`, `${DEST_SKIN_DIR}/${type}/imagecolor/${imageColor}.css`);
    });
};

const doTheme = (type) => {
    THEME_TYPE.forEach(theme => {
        // styles/gem/skin/{type}/theme/{theme}/color.css に出力
        compileSass(`${SRC_SCSS_DIR}/${type}/theme/${theme}.scss`, `${DEST_SKIN_DIR}/${type}/theme/${theme}/color.css`);
    });
};

const doErrorHtml = () => {
    // errorhtml 用の css をコンパイル
    compileSass(`${SRC_SCSS_DIR}/error/base/styles.scss`, `${DEST_ERROR_HTML_DIR}/base/styles.css`);
    compileSass(`${SRC_SCSS_DIR}/error/flat/styles.scss`, `${DEST_ERROR_HTML_DIR}/flat/styles.css`);
};

if (isImageColorOnly) {
    console.log('Compile only image color css.');
    SKIN_TYPE.forEach(type => doImageColor(type));

} else {
    console.log('Compile all css.');
    SKIN_TYPE.forEach(type => {
        doBase(type);
        doTheme(type);
        doImageColor(type);
    });

    doErrorHtml();
}
