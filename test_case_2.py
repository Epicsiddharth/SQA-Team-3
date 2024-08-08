from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

def test_case_2():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        main_heading = driver.find_element(By.ID, 'firstHeading').text
        assert main_heading == 'English language'
        print('Test Case 2 Passed: Main heading is correct')
    except Exception as e:
        print(f'Test Case 2 Failed: {e}')

    driver.quit()
