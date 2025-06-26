package Software.Portal.web.service.serviceImpl;

import Software.Portal.web.constant.RequestStatus;
import Software.Portal.web.dto.UserDto;
import Software.Portal.web.entity.Roll;
import Software.Portal.web.entity.User;
import Software.Portal.web.repository.UserRepository;
import Software.Portal.web.service.RollService;
import Software.Portal.web.service.UserService;
import Software.Portal.web.utill.CommonResponse;
import Software.Portal.web.utill.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordHashing passwordHashing;

    private final RollService rollService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private String code;

    private long codeTimestamp;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String MyEmail;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordHashing passwordHashing, RollService rollService, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordHashing = passwordHashing;
        this.rollService = rollService;
        this.mailSender = mailSender;
    }

    public void clearCodeIfExpired() {
        if (code != null && (System.currentTimeMillis() - codeTimestamp) > 120000) { // 120000 ms = 2 minutes
            code = null;
        }
    }


    @Override
    public CommonResponse saveUser(UserDto userDto) {
        CommonResponse commonResponse = new CommonResponse();
        try{
            List<String> validationList = this.userValidation(userDto);
            if (!validationList.isEmpty()) {
                commonResponse.setStatus(false);
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            User user1 = userRepository.findByEmail(userDto.getEmail());

            if (user1 != null) {
              commonResponse.setStatus(false);
              commonResponse.setCommonMessage("This email already use someone else");
              return commonResponse;
            }
            if(user1 == null) {
                User user = UserDtoIntoUser(userDto);
                userRepository.save(user);
                commonResponse.setStatus(true);
                commonResponse.setCommonMessage("User Successfully Registered!");
            }



        }catch (Exception e){
            LOGGER.error("************************** Exception User Service getByUserName ******************" + e.getMessage());
        }
        return commonResponse;
    }




    @Override
    public CommonResponse getDetalisUser(String email, String password) {
        CommonResponse commonResponse = new CommonResponse();
        UserDto userDto;
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
           if(user.getRequestStatus().equals(RequestStatus.APPROVED)){
               if (passwordHashing.checkPassword(password, user.getPassword())) {
                   userDto = UserIntoUserDto(user);
                   commonResponse.setStatus(true);
                   commonResponse.setPayload(Collections.singletonList(userDto));
                   commonResponse.setCommonMessage("Successfully logged in");
               } else {
                   commonResponse.setStatus(false);
                   commonResponse.setCommonMessage("Incorrect password");
               }
           }else {
               commonResponse.setStatus(false);
               commonResponse.setCommonMessage("Your Request Not Approved yet");
           }
            } else {
                commonResponse.setStatus(false);
                commonResponse.setCommonMessage("User name is incorrect");
            }
        } catch (Exception e) {

            LOGGER.error("Error during user authentication", e);
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("An error occurred while processing the request.");
        }

        return commonResponse;
    }




    @Override
    public User findBySiteUserId(String userId) {

      return    userRepository.getByUserId(userId);



    }

    @Override
    public CommonResponse requestApproved(String userId) {
        CommonResponse commonResponse = new CommonResponse();
      try {
          User user = userRepository.findById(userId).get();
          user.setRequestStatus(RequestStatus.APPROVED);
          userRepository.save(user);
          commonResponse.setStatus(true);
          commonResponse.setCommonMessage("User successfully approved");
          return commonResponse;
      }catch (Exception e){
          commonResponse.setStatus(false);
          LOGGER.error("Error during user approval", e);
      }
      return commonResponse;
    }

    @Override
    public UserDto findById(String userId) {
        User user =userRepository.findById(userId).get();
        UserDto userDto;
        userDto = UserIntoUserDto(user);
        return userDto;
    }

    @Override
    public CommonResponse sendCode(String email) {
        CommonResponse commonResponse = new CommonResponse();
       boolean x = CommonValidation.stringNullValidation(email);
       if (x) {
           commonResponse.setStatus(false);
           commonResponse.setCommonMessage("Please enter  email address");
           return commonResponse;
       }else {
           User user = userRepository.findByEmail(email);
           if (user != null) {
               SimpleMailMessage message = new SimpleMailMessage();
               Random random = new Random();
               this.code = String.format("%06d", random.nextInt(1000000)); // Ensures 6 digits
               this.codeTimestamp = System.currentTimeMillis(); // Record the current time
               message.setTo(user.getEmail());
               message.setSubject("Recover Password");
               message.setText("hello world" + code );
               message.setFrom(MyEmail);
               mailSender.send(message);
               commonResponse.setStatus(true);
               commonResponse.setCommonMessage("Your code sending to mail check it");
               return commonResponse;
           }else {
               commonResponse.setStatus(false);
               commonResponse.setCommonMessage("Your email address is incorrect");
               return commonResponse;
           }
       }
    }

    @Override
    public CommonResponse recover(String sendCode,String email,String password) {
        CommonResponse commonResponse = new CommonResponse();
        User  user1 = new User();
       boolean y = CommonValidation.stringNullValidation(sendCode);
        boolean x = CommonValidation.stringNullValidation(password);
        if (x) {
            commonResponse.setStatus(false);
            commonResponse.setCommonMessage("Please enter your new password");
            return commonResponse;
        }

       if (y) {
           commonResponse.setStatus(false);
           commonResponse.setCommonMessage("Please enter Recover Code");
           return commonResponse;
       }
       if (code != null) {
           User user = userRepository.findByEmail(email);
           if (user != null) {
               if(code.equals(sendCode)) {
                   user.setPassword(passwordHashing.hashPassword(password));
                   userRepository.save(user);
                   commonResponse.setStatus(true);
                   code = null;
                   commonResponse.setCommonMessage("Successfully recovered");
                   return commonResponse;

               }else{
                   commonResponse.setStatus(false);
                   commonResponse.setCommonMessage("Incorrect Recover Code !! Try again");
                   return commonResponse;
               }

           }else{
               commonResponse.setStatus(false);
               commonResponse.setCommonMessage("Your email address is incorrect");
               return commonResponse;
           }
       }else {
           commonResponse.setStatus(false);
           commonResponse.setCommonMessage("Your Recover Code is expired !! Try again");
           return commonResponse;
       }
    }


    private UserDto UserIntoUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserName(user.getUserName());
        userDto.setUserId(String.valueOf(user.getUserId()));
        userDto.setEmail(user.getEmail());
        userDto.setCommonStatus(user.getCommonStatus());
        userDto.setRequestStatus(user.getRequestStatus());
        Roll firstRoll = user.getRoll().stream().findFirst().orElse(null);
        if (firstRoll != null) {
            userDto.setRollDto(rollService.castSiteRollsDTOIntoSiteRolls(firstRoll.getRollId()));
        }        return userDto;
    }

    private User UserDtoIntoUser(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        String hashedPassword = passwordHashing.hashPassword(userDto.getPassword());
        user.setCommonStatus(userDto.getCommonStatus());
        user.setEmail(userDto.getEmail());
        user.setPassword(hashedPassword);
        user.setRequestStatus(RequestStatus.APPROVED);
        Roll roll = rollService.findByRollID(userDto.getRollDto().getRollId());
        user.setRoll(Set.of(roll)); // Java 9+, or use Collections.singleton(roll)
         return user;
    }

    private List<String> userValidation(UserDto userDto) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(userDto.getUserName()))
            validationList.add("Initial name field is empty");
        return validationList;
    }

}
