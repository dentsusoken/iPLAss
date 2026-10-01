const quill = window.Quill;

if (!quill) {
	throw new Error("Quill UMD must be loaded before the table module.");
}

export default quill;