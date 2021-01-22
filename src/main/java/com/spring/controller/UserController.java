package com.spring.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.customException.EmailExistsException;
import com.spring.customException.UsernameExistsException;
import com.spring.entity.User;
import com.spring.security.UserCustody;
import com.spring.security.util.JWTTokenProvider;
import com.spring.security.util.SecurityConstant;
import com.spring.service.UserService;
import com.spring.util.HttpResponse;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTTokenProvider jwtTokenProvider;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<HttpResponse> register(@RequestBody User user) {

		try {
			User newUser = userService.register(user);
			return new ResponseEntity<>(new HttpResponse(200, HttpStatus.OK, null, "Success", newUser), HttpStatus.OK);

		} catch (UsernameExistsException | EmailExistsException e) {
			return new ResponseEntity<>(new HttpResponse(400, HttpStatus.BAD_REQUEST, ExceptionUtils.getStackTrace(e),
					e.getMessage(), null), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new HttpResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage(), null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<HttpResponse> login(@RequestBody User user) {

		try {

			authenticate(user.getUsername(), user.getPassword());

			User loginUser = userService.findByUsername(user.getUsername());
			UserCustody userCustody = new UserCustody(loginUser);

			HttpHeaders header = getHeaderWithJwt(userCustody);

			return new ResponseEntity<>(new HttpResponse(200, HttpStatus.OK, null, "Success", loginUser), header,
					HttpStatus.OK);
		} catch (BadCredentialsException e) {

			return new ResponseEntity<>(new HttpResponse(400, HttpStatus.BAD_REQUEST, ExceptionUtils.getStackTrace(e),
					e.getMessage(), null), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {

			return new ResponseEntity<>(
					new HttpResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage(), null),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public ResponseEntity<HttpStatus> showUser() {

		try {

			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String testException() throws EmailExistsException {

		throw new EmailExistsException("This email is already taken");
	}

	private HttpHeaders getHeaderWithJwt(UserCustody userCustody) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userCustody));
		return httpHeaders;
	}

	private void authenticate(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
