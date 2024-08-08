from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

def test_case_4():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        history_section = driver.find_element(By.ID, 'History').text
        assert 'history' in history_section.lower()
        print('Test Case 4 Passed: History section is present')
    except Exception as e:
        print(f'Test Case 4 Failed: {e}')

    driver.quit()
