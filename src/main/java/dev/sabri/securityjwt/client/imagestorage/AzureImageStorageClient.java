package dev.sabri.securityjwt.client.imagestorage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobStorageException;
import dev.sabri.securityjwt.system.exception.CustomBlobStorageException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
public class AzureImageStorageClient implements ImageStorageClient {

    private final BlobServiceClient blobServiceClient;

    public AzureImageStorageClient(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public String uploadImage(String containerName, String originalImageName, InputStream data, long length) {


        try{
            // Get the BlobContainerClient object to interact with the container
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Rename the image file to a unique name
            String newImageName = UUID.randomUUID().toString() + originalImageName.substring(originalImageName.lastIndexOf("."));

            // Get the BlobClient object to interact with the specified blob
            BlobClient blobClient = blobContainerClient.getBlobClient(newImageName);

            // Upload the image file to the blob
            blobClient.upload(data, length, true);

            return blobClient.getBlobUrl();
        }catch (BlobStorageException e){
            throw new CustomBlobStorageException("Failed to upload the image", e);
        }
    }
}
