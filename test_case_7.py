from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

def test_case_7():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        table = driver.find_element(By.CSS_SELECTOR, 'table.wikitable')
        assert table is not None
        print('Test Case 7 Passed: The specified table is present on the page')
    except Exception as e:
        print(f'Test Case 7 Failed: {e}')

    driver.quit()
