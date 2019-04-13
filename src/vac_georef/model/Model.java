package vac_georef.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vacModele.FichierPDF;
import vacModele.PagePDF;

public class Model {
	private List<File> fileList = new ArrayList<>() ;
	private File selectedDirectory;
	HashMap<FichierPDF, ArrayList<PagePDF>> MapPDF = 
			new HashMap<FichierPDF, ArrayList<PagePDF>>();
	private ArrayList<FichierPDF> pdfArray = new ArrayList<FichierPDF>();
	
	private final static Model instance = new Model();	
	
	public static Model getInstance() {
		return instance;
	}
	public List<File> getFileList() {
		return fileList;
	}
	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}
	public File getSelectedDirectory() {
		return selectedDirectory;
	}
	public void setSelectedDirectory(File selectedDirectory) {
		this.selectedDirectory = selectedDirectory;
	}
	
	public void computeMapPDF() throws Exception {
		if (fileList != null) {
			for (File file : fileList) {
				FichierPDF pdf = new FichierPDF(file);
				pdfArray.add(pdf);
				ArrayList<PagePDF> page_list = pdf.toPagePDF();
				MapPDF.put(pdf, page_list);
			}
		}
		else {
			System.out.println("filelist null!!");
		}
	}
	public HashMap<FichierPDF, ArrayList<PagePDF>> getMapPDF() {
		return MapPDF;
	}
	public ArrayList<FichierPDF> getPdfArray() {
		return pdfArray;
	}
	public void setPdfArray(ArrayList<FichierPDF> pdfArray) {
		this.pdfArray = pdfArray;
	}
	
	public void computePDFArray() {
		if (fileList != null) {
			for (File file : fileList) {
				FichierPDF pdf = new FichierPDF(file);
				pdfArray.add(pdf);//met à jour pdfArray
			}
		}
		else {
			System.out.println("filelist null!!");
		}
	}
	
}
