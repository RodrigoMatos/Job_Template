package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {	

	public byte[] decompressArrayWithoutDeflater(byte[] array) throws Exception{
		ByteArrayInputStream byteInputStream = null;
		ZipInputStream zis = null;
		ByteArrayOutputStream bos = null;
		try {
			byteInputStream = new ByteArrayInputStream(array);
			zis = new ZipInputStream(byteInputStream);
			bos = new ByteArrayOutputStream(array.length);
			while((zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[2048];  
				while ((count = zis.read(data, 0, 2048))!= -1) {
					bos.write(data, 0, count);
				}	           
			}
			zis.close();
			bos.flush();
			bos.close();
		} catch(Exception e) {
			throw e;
		}	     
		return bos.toByteArray();
	}
	
	public byte[] compressListaArquivos(List<File> arquivos) throws Exception{
        
	    // Create a buffer for reading the files 
	    byte[] buf = new byte[1024]; 
	    byte[] stream = null;
	    File arquivoFinal = new File(FileUtil.getNomeArquivoTemp(".zip"));
	    try { 
	    	// Create the ZIP file 
	    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arquivoFinal)); 
	    	// Compress the files 
	    	for (File arquivo: arquivos) { 
	    		FileInputStream in = new FileInputStream(arquivo); 
	    		// Add ZIP entry to output stream. 
	    		out.putNextEntry(new ZipEntry(arquivo.getName())); 
	    		// Transfer bytes from the file to the ZIP file 
	    		int len; 
	    		while ((len = in.read(buf)) > 0) { 
	    			out.write(buf, 0, len); 
	    		} 
	    		// Complete the entry 
	    		out.closeEntry(); 
	    		in.close(); 
	    		arquivo.delete();
	    	} 
	    	// Complete the ZIP file 
	    	out.close(); 
	    	InputStream is = new FileInputStream(arquivoFinal);
   			stream = new byte[(int)arquivoFinal.length()];        
   		    int offset = 0;
   		    int numRead = 0;
   		    while (offset < stream.length && (numRead= is.read(stream, offset, stream.length-offset)) >= 0) {
   		        offset += numRead;
   		    }  
   		    is.close();  
   		    arquivoFinal.delete();
	    }catch (IOException e) { 
	    	throw e;
	    } 	 
	    return stream;
    }

	public byte[] decompressArray(byte[] array, boolean retornaNullErro) throws Exception {	
		
	    Inflater decompressor;
	    ByteArrayOutputStream bos;

        try{
        	
        	decompressor = new Inflater();
            decompressor.setInput(array);

            bos = new ByteArrayOutputStream(array.length);
        	
            byte[] buf = new byte[1024];
            while (!decompressor.finished()){
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
            bos.close();
            
            return bos.toByteArray();
            
        } catch (IOException e){
            throw e;
        } catch (DataFormatException e){
        	if(retornaNullErro){
        		return null;
        	} else {
        		throw e;	
        	}        	           
        }        
    }
}
