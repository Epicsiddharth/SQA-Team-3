from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

def test_case_8():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        images = driver.find_elements(By.TAG_NAME, 'img')
        assert len(images) > 0
        print('Test Case 8 Passed: At least one image is present on the page')
    except Exception as e:
        print(f'Test Case 8 Failed: {e}')

    driver.quit()
