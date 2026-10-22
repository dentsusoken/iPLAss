import TableUp, { defaultCustomSelect, TableSelection, TableMenuContextmenu, TableResizeLine } from "quill-table-up";
import QuillResize from "quill-resize-module";
const quill = window.Quill;

if (!quill) {
	throw new Error("Quill UMD must be loaded before quill plugins.");
}

// quill-table-up 3.6.0はテーブル削除時にテーブルへの参照をクリアするが、
// そのイベントではセル選択オーバーレイを非表示にしない。
class TableSelectionWithCleanup extends TableSelection {
	static moduleName = TableSelection.moduleName;
	removalObserver;
	tableSelectHandler(event) {
		const target = event.target instanceof Element ? event.target : null;
		const table = target?.closest('table');
		if (table && !table.classList.contains('ql-table')) {
			// GEMではエディタをレイアウト用テーブルに配置しているため、外側のテーブルをQuillのテーブルとして扱わない。
			this.hide();
			return;
		}
		return TableSelection.prototype.tableSelectHandler.call(this, event);
	}

	constructor(tableModule, editor, options) {
		super(tableModule, editor, options);
		this.removalObserver = new MutationObserver(() => {
			const tableWasRemoved = this.table && !this.quill.root.contains(this.table);
			const selectedCellWasRemoved = this.selectedTds.some(cell => !cell.domNode.isConnected);
			if (tableWasRemoved || selectedCellWasRemoved) {
				this.hide();
			}
		});
		this.removalObserver.observe(this.quill.root, { childList: true, subtree: true });
	}

	destroy() {
		this.removalObserver.disconnect();
		super.destroy();
	}
}

class TableResizeLineWithTableGuard extends TableResizeLine {
	static moduleName = TableResizeLine.moduleName;

	tableSelectHandler(event) {
		const target = event.target instanceof Element ? event.target : null;
		const table = target?.closest('table');
		if (table && !table.classList.contains('ql-table')) {
			this.setSelectionTable(undefined);
			return;
		}
		return TableResizeLine.prototype.tableSelectHandler.call(this, event);
	}
}

quill.register({ [`modules/${TableUp.moduleName}`]: TableUp }, true);
quill.register("modules/resize", QuillResize, true);

// Quill標準のVideo.html()はgetSemanticHTML()時にiframeをリンクへ変換するため、
// GEMでは動画埋め込みとして再読込可能なHTMLを出力する。
const Video = quill.import("formats/video");
class VideoWithSemanticHtml extends Video {
	html() {
		const iframe = document.createElement("iframe");
		iframe.className = ["ql-video", ...Array.from(this.domNode.classList)
			.filter(className => /^ql-align-(center|right|justify)$/.test(className))].join(" ");

		const src = this.statics.sanitize(this.domNode.getAttribute("src") || "");
		iframe.setAttribute("src", src);
		iframe.setAttribute("frameborder", "0");
		iframe.setAttribute("allowfullscreen", "true");
		["width", "height"].forEach(attribute => {
			if (this.domNode.hasAttribute(attribute)) {
				iframe.setAttribute(attribute, this.domNode.getAttribute(attribute));
			}
		});

		return iframe.outerHTML;
	}
}
quill.register({ "formats/video": VideoWithSemanticHtml }, true);

// quill-resize-module 2.1.3はresize-inlineとresize-blockを同じクラスキー（ql-resize-style）で登録する。
// Parchmentではblock側のAttributorがinline側のキーを上書きするため、保存済みの画像配置がresize-blockとしてインポートされる。
const Parchment = quill.import("parchment");
const ImageResizeAlignment = new Parchment.ClassAttributor("resize-inline", "ql-resize-style", {
	scope: Parchment.Scope.INLINE,
	whitelist: ["left", "center", "right", "full"],
});
quill.register(ImageResizeAlignment, true);

window.scriptContext.gem.richtext.quill.tableUp = { TableUp, defaultCustomSelect, TableSelection: TableSelectionWithCleanup, TableMenuContextmenu, TableResizeLine: TableResizeLineWithTableGuard };