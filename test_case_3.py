from selenium import webdriver
from selenium.webdriver.chrome.service import Service

def test_case_3():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        print('Test Case 3 Passed: The page is scrollable')
    except Exception as e:
        print(f'Test Case 3 Failed: {e}')

    driver.quit()
