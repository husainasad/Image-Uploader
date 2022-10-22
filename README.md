## Image-Uploader
Image Uploader is a basic android application integrated with a flask server.<br>
The user can use the application to click images, add tags and upload the clicked image to the server.<br>
The server receives the image array and tag information in a post request.<br>
The image array is then converted back into the original image and stored in its respective 'tag' folder.<br>

### Instructions to setup
Use command 'python index.py' in cmd terminal to run the flask local server<br>
Copy the localhost ip address from terminal<br>
Open 'ImageUploader' project in android studio<br>
Replace private String URL in app>>src>>main>>java>>com>>example>>imageuploader>>uploadActivity with the ip address copied from flask server<br>
Run the android application<br>