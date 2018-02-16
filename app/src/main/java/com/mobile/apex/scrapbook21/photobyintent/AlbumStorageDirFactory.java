package com.mobile.apex.scrapbook21.photobyintent;

import java.io.File;

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
