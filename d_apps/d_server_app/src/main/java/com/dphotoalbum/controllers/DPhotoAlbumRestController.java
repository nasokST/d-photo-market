package com.dphotoalbum.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dphotoalbum.config.PhotoCategoryType;
import com.dphotoalbum.objects.DPhoto;
import com.dphotoalbum.objects.DPhotoCommentsInput;
import com.dphotoalbum.objects.DPhotoInput;
import com.dphotoalbum.services.PhotoAlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.shumencoin.node.NodeApplication;

@RestController
public class DPhotoAlbumRestController {

	@Autowired
	public PhotoAlbumService photoAlbumService;

//    @RequestMapping("/node")
//    public Node index() {
//	return node;
//    }

	@GetMapping("/categories/all")
	public ResponseEntity<?> getNodeId() {
		return new ResponseEntity<Object>(photoAlbumService.getAllcategories(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/dphoto", headers = ("content-type=multipart/*"), produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadMultipleFile(@RequestParam("file") List<MultipartFile> inputFiles,
			@RequestParam("category") List<PhotoCategoryType> categories, 
			@RequestParam("pk") List<String> keys) {

		List<DPhotoInput> dphotos = new ArrayList<DPhotoInput>();

		try {
			boolean listSizeEqual = (inputFiles.size() == categories.size() && inputFiles.size() == keys.size());

			if (!listSizeEqual) {
				new ResponseEntity<Object>("Missing parameters", HttpStatus.BAD_REQUEST);
			}

			int inputFilesListSize = inputFiles.size();
			for (int idx = 0; idx < inputFilesListSize; ++idx) {

				DPhotoInput dphoto = new DPhotoInput();

				dphoto.setCategory(categories.get(idx));
				dphoto.setPk(keys.get(idx));
				dphoto.setFileSize(inputFiles.get(idx).getSize());
				dphoto.setFile(inputFiles.get(idx).getBytes());

				dphotos.add(dphoto);
			}
			
			if (!photoAlbumService.addPhotos(dphotos)) {
				new ResponseEntity<Object>("ERROR uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			new ResponseEntity<Object>("ERROR uploading file", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>("OK", HttpStatus.OK);
	}
	
	@PostMapping("/dphoto/comment")
	public ResponseEntity<?> setPhotoComment(@RequestBody DPhotoCommentsInput photoComments) {
		if(!photoAlbumService.addComments(photoComments)) {
			new ResponseEntity<Object>("ERROR uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>("OK", HttpStatus.OK);
	}
	
	
	

//    @GetMapping("/node/chain-id")
//    public ResponseEntity<?> getChainId() {
//	return new ResponseEntity<Object>(node.getBlockchain().getChainId(), HttpStatus.OK);
//    }
//
//    @RequestMapping("/balances")
//    public ResponseEntity<?> getConfirmedBalances() {
//	return new ResponseEntity<Object>("getConfirmedBalances() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping("/blocks")
//    public List<BlockData> getAllBlocks() {
//	return node.getBlockchain().getChain().getBlocks();
//    }
//
//    @RequestMapping("/blocks/{index}")
//    public ResponseEntity<?> getBlockByIndex(@PathVariable("index") BigInteger index) {
//	try {
//	    BlockData block = node.getBlockchain().getChain().getBlocks().get(Integer.valueOf(index.toString()));
//	    return new ResponseEntity<Object>(block, HttpStatus.OK);
//	} catch (Exception e) {
//	    return new ResponseEntity<Object>("Invalid block: " + index, HttpStatus.BAD_REQUEST);
//	}
//    }
//
//    // ====== TRANSACTIONS =====
//    @RequestMapping("/transactions/pending")
//    public List<TransactionData> getAllPendingTransactions() {
//	return node.getBlockchain().getChain().getPendingTransactions();
//    }
//
//    @RequestMapping("/transactions/confirmed")
//    public List<TransactionData> getAllConfirmedTransactions() {
//	return node.getBlockchain().listConfirmedTransaction();
//    }
//
//    @RequestMapping("/transactions/{transactionHash}")
//    public ResponseEntity<?> getTransactionByHash(@PathVariable("transactionHash") String hash) {
//	try {
//	    TransactionData transaction = node.getBlockchain().getTransactionByHash(hash);
//	    return new ResponseEntity<Object>(transaction, HttpStatus.OK);
//	} catch (Exception e) {
//	    return new ResponseEntity<Object>("Invalid transaction hash: " + hash, HttpStatus.BAD_REQUEST);
//	}
//    }
//
//    @PostMapping("/transactions/send/faucet")
//    public ResponseEntity<?> getTransactionSend(@RequestBody String address) {
//	String addressString = address.substring(0, address.length() - 1);
//
//	TransactionData td = node.getBlockchain().generateFaucetTransaction(addressString);
//
//	if (null != td) {
//	    // notify all current node peers
//	    NodeApplication.sendTransactionToAllPeers(node, td, "");
//
//	    return new ResponseEntity<Object>("Success", HttpStatus.OK);
//	} else {
//	    return new ResponseEntity<Object>("Transaction not created", HttpStatus.BAD_REQUEST);
//	}
//    }
//
//    @PostMapping("/transactions/send/{senderNodeId}")
//    public ResponseEntity<?> getTransactionSend(@RequestBody TransactionData newTransaction,
//	    @PathVariable String senderNodeId) {
//
//	String senderNodeUrl = node.getNode().getPeers().get(senderNodeId);
//	if (null == senderNodeUrl) {
//	    return new ResponseEntity<Object>("Transaction not created", HttpStatus.BAD_REQUEST);
//	}
//
//	ShCError error = node.getBlockchain().pendingTransactionsAdd(newTransaction);
//	if (ShCError.NO_ERROR != error) {
//	    return new ResponseEntity<Object>("Transaction not created", HttpStatus.BAD_REQUEST);
//	}
//
//	// notify all current node peers
//	NodeApplication.sendTransactionToAllPeers(node, newTransaction, senderNodeId);
//
//	return new ResponseEntity<Object>("Success", HttpStatus.OK);
//    }
//
//    @RequestMapping("/transactions/balances")
//    public List<BalanceBean> getBalances() {
//	return node.getBlockchain().getBalances();
//    }
//
//    // ====== WALLET =====
//    @PostMapping("/wallet/account")
//    public ResponseEntity<?> walletGetAccount(@RequestBody String password) {
//	
//	if("=".equals(password.substring(password.length()-1, password.length()))) {
//	    password = password.substring(0, password.length()-1);
//	}
//	
//	byte[] privateKey = Crypto.generatePrivateKey();
//	byte[] address = Crypto.generateAddressByPrivateKey(privateKey);
//
//	try {
//	    String encryptedPassword = Crypto.encryptionPrivateKey(password, privateKey, address);
//	    return new ResponseEntity<Object>(encryptedPassword, HttpStatus.OK);
//	} catch (DataLengthException e) {
//	} catch (InvalidCipherTextException e) {
//	} catch (JsonProcessingException e) {
//	}
//
//	return new ResponseEntity<Object>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @CrossOrigin
//    @PostMapping("/wallet/transaction")
//    public ResponseEntity<?> walletSendTransaction(@RequestBody WalletSendTransactionData walletSendTransactionData) {
//
//	walletSendTransactionData.getTransactionData().setFrom(walletSendTransactionData.getCryptoData().address);
//	ShCError error = NodeApplication.OnWalletNewTransaction(node, walletSendTransactionData);
//	if (ShCError.NO_ERROR == error) {
//	    return new ResponseEntity<Object>(error, HttpStatus.OK);
//	}
//
//	return new ResponseEntity<Object>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // ====== ADDRESS =====
//    @RequestMapping("/address/{address}/transactions")
//    public ResponseEntity<?> addressGetTransactions() {
//	// TODO
//	return new ResponseEntity<Object>("addressGetTransactions() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping("/address/{address}/balance")
//    public ResponseEntity<?> addressGetBalance() {
//	// TODO
//	return new ResponseEntity<Object>("addressGetBalance() NOT IMPLEMENTED ", HttpStatus.BAD_REQUEST);
//    }
//
//    // ====== PEARS =====
//    @RequestMapping("/peers")
//    public ResponseEntity<?> getPeers() {
//	return new ResponseEntity<Object>(node.getNode().getPeers(), HttpStatus.OK);
//    }
//
//    @PostMapping("/peers/connect")
//    public ResponseEntity<?> peerAskToConnect(@RequestBody NotificationBaseData peerConnectingInformation) {
//	ShCError error = NodeApplication.OnPeerAskToConnect(node, peerConnectingInformation);
//	if (ShCError.NO_ERROR == error) {
//	    return new ResponseEntity<Object>(error, HttpStatus.OK);
//	}
//
//	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @PostMapping("/peers/notify-new-block")
//    public ResponseEntity<?> getPeersNotifyForNewBlock(@RequestBody NotificationBaseData notificationData) {
//
//	ShCError error = NodeApplication.OnNewBlockNotification(node, notificationData);
//	if (ShCError.NO_ERROR == error) {
//	    return new ResponseEntity<Object>(error, HttpStatus.OK);
//	}
//
//	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    // ====== MINING =====
//    @GetMapping("/mining/job-request/{minerAddress}")
//    public ResponseEntity<?> getMiningAskJob(@PathVariable("minerAddress") String minerAddress) {
//
//	MiningJobData miningJob = new MiningJobData();
//	ShCError error = node.getBlockchain().getNewMiningJob(minerAddress, miningJob);
//	if (ShCError.NO_ERROR == error) {
//	    return new ResponseEntity<Object>(miningJob, HttpStatus.OK);
//	}
//
//	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @PostMapping("/mining/submit-new-block")
//    public ResponseEntity<?> getMiningSubmitNewBlock(@RequestBody MiningJobData minedBlock) {
//
//	BlockData newBlock = new BlockData();
//	ShCError error = NodeApplication.OnSubmitMinedBlock(node, minedBlock, newBlock);
//	if (ShCError.NO_ERROR == error) {
//	    return new ResponseEntity<Object>(newBlock, HttpStatus.OK);
//	}
//
//	return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
//    }
}