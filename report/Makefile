# Generic make file for LaTeX: requires GNU make

TEXFILE	= finalReport.tex

.PHONY: dvi ps pdf clean

pdf:	$(TEXFILE:.tex=.pdf)
ps:	$(TEXFILE:.tex=.ps)
dvi:	$(TEXFILE:.tex=.dvi)

%.dvi: %.tex
	( \
	latex $<; \
	while grep -q "Rerun to get cross-references right." $(<:.tex=.log); \
	do \
		latex $<; \
	done \
	)

%.ps: %.dvi
	dvips -q -t a4 $< -o $(<:.dvi=.ps)

%.pdf: %.ps
	ps2pdf -dPDFSETTINGS=/prepress $<

clean:
	@rm -f \
	$(TEXFILE:.tex=.aux) \
	$(TEXFILE:.tex=.log) \
	$(TEXFILE:.tex=.out) \
	$(TEXFILE:.tex=.dvi) \
	$(TEXFILE:.tex=.pdf) \
	$(TEXFILE:.tex=.ps)
