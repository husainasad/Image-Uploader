from flask import Flask
from flask import request
import base64
import os

app = Flask(__name__)

# @app.route("/")
# def home():
# 	return "Hello, Welcome to Flask Server"
# curr_dir = os.getcwd()

@app.route('/', methods=['POST'])
def handle_request():
	imgTag = request.form['categoryName']
	imgName = request.form['imageName']
	imgBase64 = request.form['imageBase']
	base_to_Image(imgTag, imgName, imgBase64)
	return "Image Uploaded Successfuly"

def base_to_Image(imageTag, imageName, imageBase64):
	# path = curr_dir
	try:
		os.makedirs(imageTag)
	except:
		print("folder already exists")
	newPath = os.path.join(os.getcwd(), imageTag)
	imgFile = open(os.path.join(newPath, imageName), 'wb')
	imgFile.write(base64.b64decode((imageBase64)))
	imgFile.close()

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)