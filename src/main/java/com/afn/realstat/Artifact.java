package com.afn.realstat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.afn.realstat.framework.SpringApplicationContext;

@Entity
@Table(name = "artifact", indexes = { @Index(name = "idx_identifier", columnList = "identifier"),
		@Index(name = "idx_category", columnList = "category") })
/*
 * TODO this class needs to be finished
 */
public class Artifact extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<Artifact> classType = Artifact.class;
	private static final int blobSize = 25000000;
	public static ArtifactRepository repo;

	private String identifier;
	private String category;
	private String description;
	private String fileName;
	
	@Lob
	@Column(length = blobSize) // for now set to 25Mb
	private Blob content;

	private String mimeType;
	private String mimeSubtype;
	private Date created;
	private Date deleteAfter;

	public Artifact() {
	}

	public Artifact(File file, String category) {
		this(file, category, null);
	}

	public Artifact(File file, String category, String description) {
		fileName = file.getName();
		identifier = FilenameUtils.getBaseName(fileName);
		this.category = category;
		if (description == null) {
			this.description = identifier;
		}
		byte[] fileBytes;
		try {
			mimeType = Files.probeContentType(file.toPath());
			fileBytes = Files.readAllBytes(file.toPath());
			this.content = new SerialBlob(fileBytes);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.created = new Date();
		this.deleteAfter = AfnDateUtil.never();
	}

	@Override
	public void clean() {
	}

	@Override
	public String toString() {
		String str = description + " " + fileName;
		return str;
	}

	@Override
	public Example<Artifact> getRefExample() {
		Artifact sample = new Artifact();
		sample.identifier = identifier;
		sample.category = category;
		Example<Artifact> e = Example.of(sample);
		return e;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeSubtype() {
		return mimeSubtype;
	}

	public void setMimeSubtype(String mimeSubtype) {
		this.mimeSubtype = mimeSubtype;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleteAfter() {
		return deleteAfter;
	}

	public void setDeleteAfter(Date deleteAfter) {
		this.deleteAfter = deleteAfter;
	}
	
	public int getBlobSize() {
		return blobSize;
	}

	public static ArtifactRepository getRepo() {
		if (repo == null) {
			repo =  (ArtifactRepository) SpringApplicationContext.getBean("artifactRepository");
		}
		return repo;
	}

	@Override
	public void save() {
		getRepo().save(this);
		
	}

	@Override
	public void saveOrUpdate() {
		getRepo().save(this);
	}
}
