from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

def test_case_9():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        footer = driver.find_element(By.ID, 'footer')
        privacy_policy_link = footer.find_element(By.LINK_TEXT, 'Privacy policy')
        assert privacy_policy_link is not None
        print('Test Case 9 Passed: Privacy policy link is present in the footer')
    except Exception as e:
        print(f'Test Case 9 Failed: {e}')

    driver.quit()
