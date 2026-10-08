import TableUp, { defaultCustomSelect, TableSelection, TableMenuContextmenu, TableResizeLine } from "quill-table-up";
import QuillResize from "quill-resize-module";
const quill = window.Quill;

if (!quill) {
	throw new Error("Quill UMD must be loaded before quill plugins.");
}

// quill-table-up 3.6.0 clears its table reference when the table is removed,
// but does not hide the cell-selection overlay on that event.
class TableSelectionWithCleanup extends TableSelection {
	static moduleName = TableSelection.moduleName;
	removalObserver;
	tableSelectHandler(event) {
		const target = event.target instanceof Element ? event.target : null;
		const table = target?.closest('table');
		if (table && !table.classList.contains('ql-table')) {
			// GEM embeds the editor in a layout table; do not treat that outer table as a Quill table.
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

// quill-resize-module 2.1.3 registers resize-inline and resize-block with the
// same class key (ql-resize-style). The block attributor overwrites the inline
// key in Parchment, so saved image alignment is imported as resize-block.
const Parchment = quill.import("parchment");
const ImageResizeAlignment = new Parchment.ClassAttributor("resize-inline", "ql-resize-style", {
	scope: Parchment.Scope.INLINE,
	whitelist: ["left", "center", "right", "full"],
});
quill.register(ImageResizeAlignment, true);

window.scriptContext.gem.richtext.quill.tableUp = { TableUp, defaultCustomSelect, TableSelection: TableSelectionWithCleanup, TableMenuContextmenu, TableResizeLine: TableResizeLineWithTableGuard };