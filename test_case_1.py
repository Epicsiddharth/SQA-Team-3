from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_case_1():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        assert 'English language - Wikipedia' in driver.title
        print('Test Case 1 Passed: Page title is correct')
    except AssertionError:
        print('Test Case 1 Failed: Page title is incorrect')

    driver.quit()
